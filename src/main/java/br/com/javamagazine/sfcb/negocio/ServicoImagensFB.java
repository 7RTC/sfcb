package br.com.javamagazine.sfcb.negocio;

import br.com.javamagazine.sfcb.modelo.Foto;
import br.com.javamagazine.sfcb.modelo.Fotos;
import br.com.javamagazine.sfcb.modelo.Imagem;
import br.com.javamagazine.sfcb.modelo.Publicacao;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.Photo;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ServicoImagensFB extends ServicoFacebook {

    public static final int FOTOS_POR_PAGINA_PADRAO = 25;
    private static final int connectTimeout = 25000;
    private static final Logger log = Logger.getLogger(ServicoImagensFB.class.getName());

    private final int fotosPorPagina;

    public ServicoImagensFB(String accessToken) {
        this(accessToken, FOTOS_POR_PAGINA_PADRAO);
    }

    public ServicoImagensFB(String accessToken, Integer fotosPorPagina) {
        super(accessToken);
        this.fotosPorPagina = fotosPorPagina != null && fotosPorPagina > 0 ? fotosPorPagina : FOTOS_POR_PAGINA_PADRAO;
    }

    public int getFotosPorPagina() {
        return fotosPorPagina;
    }

    public Fotos listarAlbum(long albumId) {
        log.info("Lista album: " + albumId);
        log.info("Limit: " + fotosPorPagina );
        final Connection<Photo> photos = client.fetchConnection(albumId+"/photos", Photo.class,
                Parameter.with("fields", "source, picture"), Parameter.with("limit", fotosPorPagina));
        return criarFotos(photos);
    }
    
    public Fotos listarTodas() {
        log.info("Lista todas as fotos: ");
        log.info("Limit: " + fotosPorPagina );
    	final Connection<Photo> photos = client.fetchConnection("me/photos", Photo.class,
                Parameter.with("limit", fotosPorPagina), Parameter.with("fields", "source, picture"),
                Parameter.with("type", "uploaded"));
    	
    	return criarFotos(photos);
    }

    public Fotos buscarPagina(String pagina) {
        final Connection<Photo> photos = client.fetchConnectionPage(pagina, Photo.class);
        return criarFotos(photos);
    }

    public Publicacao publicarRestFB(Imagem imagem) throws IOException {
        try (InputStream input = new ByteArrayInputStream(imagem.getCorpo())) {
            final Publicacao resposta = client.publish("me/photos", Publicacao.class,
                    BinaryAttachment.with("sfcb. " + imagem.getExtensao(), input),
                    Parameter.with("message", "Criado com Social Facebook Collage Builder"));
            log.info("Resposta da mensagem retornada pelo Facebook " + resposta);
            return resposta;
        } catch (IOException e) {
            throw new IOException("Não foi possível publicar a imagem", e);
        }
    }

    public Publicacao publicarGraphAPI(Imagem imagem) throws IOException {

        /*
         * Simula um POST vindo de um form com:
         * <form enctype="multipart/form-data" "method="POST">
         *  <input name="source" type="file" />
         *  <input name="message" type="text" />
         * </form>
         */
        final HttpEntity corpo = MultipartEntityBuilder.create()
                .addBinaryBody("source", // nome da parte
                        imagem.getCorpo(), // imagem
                        ContentType.create(imagem.getMimeType()), // MIME
                        "sfcb. " + imagem.getExtensao()) // Nome da imagem
                .addTextBody("message", "Criado com Social Facebook Collage Builder")
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .build();


        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new
                    URL("https://graph.facebook.com/me/photos?access_token=" + accessToken).openConnection();
            connection.setConnectTimeout(connectTimeout);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-length", "" + corpo.getContentLength());
            log.fine(corpo.getContentType().getName() + " = " + corpo.getContentType().getValue());
            connection.addRequestProperty(corpo.getContentType().getName(), corpo.getContentType().getValue());
        } catch (IOException e) {
            throw new IOException("Não foi possível conectar a URL de upload do Facebook", e);
        }

        try (OutputStream output = connection.getOutputStream()) {
            corpo.writeTo(output);
        } catch (IOException e) {
            throw new IOException("Não foi possível fazer upload para a URL do Facebook", e);
        }

        Publicacao resposta = null;
        try (InputStream input = connection.getInputStream()) {
            String jsonResposta = lerStream(input);
            Gson gson = new Gson();
            resposta = gson.fromJson(jsonResposta, Publicacao.class);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.fine("Upload realizado com sucesso: " + resposta);
            } else {
                log.severe("Falha no upload do arquivo: " + jsonResposta);
                // TODO: Limpar
                throw new RuntimeException("Falha no upload do arquivo: " + resposta);
            }
        } catch (IOException e) {
            throw new IOException("Não foi possível ler a resposta do Facebook", e);
        }

        return resposta;
    }

    private String lerStream(InputStream input) throws IOException {
        return CharStreams.toString(new InputStreamReader(input, "UTF-8"));
    }

    private Fotos criarFotos(Connection<Photo> photos) {
        final Fotos fotos = new Fotos();
        final List<Foto> listaFotos = new ArrayList<>();
        for (Photo photo : photos.getData()) {
            listaFotos.add(new Foto(photo));
        }
        fotos.setFotos(listaFotos);
        fotos.setPaginaAnterior(photos.getPreviousPageUrl());
        fotos.setProximaPagina(photos.getNextPageUrl());

        log.info("Tamanho do array de fotos: " + listaFotos.size());

        return fotos;
    }
}
