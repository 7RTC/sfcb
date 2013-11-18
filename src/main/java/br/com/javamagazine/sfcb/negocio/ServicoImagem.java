package br.com.javamagazine.sfcb.negocio;

import br.com.javamagazine.sfcb.modelo.Imagem;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.apache.commons.codec.binary.Base64;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicoImagem {

    private static final Logger log = Logger.getLogger(ServicoImagem.class.getName());

    private final ImagesService imagesService;

    private final Pattern pattern = Pattern.compile("^data:(image/(png|jpg|jpeg));base64,");

    public ServicoImagem() {
        imagesService = ImagesServiceFactory.getImagesService();
    }

    public Imagem recuperarDaDataURL(String dataURL) {
        log.info("Base 64 String: " + dataURL);
        final Matcher matcher = pattern.matcher(dataURL);
        if(!matcher.find()) {
            throw new IllegalArgumentException("Formato da imagem invalido, apenas png e jpeg são suportados");
        }
        // Decoda o string da imagem do canvas
        // Extrai image/tipo de uma string data:image/png|;base64
        final String mimeType = matcher.group(1);
        final String corpoEmBase64 = pattern.split(dataURL)[1];
        // Descarta o mimeType e decoda o corpo da imagem
        byte[] corpo = Base64.decodeBase64(corpoEmBase64);

        return new Imagem(corpo, mimeType);
    }

    public Imagem redimensionar(Imagem original) {
        final Image originalGAE = ImagesServiceFactory.makeImage(original.getCorpo());
        /*
         * False faz o GAE manter a proporcao, logo a imagem serah redimensionada para o maior tamanho possivel que
         * caiba em um quadrado de 600 por 600
         */
        final Transform transformacao = ImagesServiceFactory.makeResize(600, 600, false);

        final Image novaGAE = imagesService.applyTransform(transformacao, originalGAE);

        return new Imagem(novaGAE.getImageData(), "image/" + novaGAE.getFormat().name().toLowerCase());
    }
}
