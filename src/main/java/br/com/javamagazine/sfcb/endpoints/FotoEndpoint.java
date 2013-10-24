package br.com.javamagazine.sfcb.endpoints;

import br.com.javamagazine.sfcb.modelo.Fotos;
import br.com.javamagazine.sfcb.modelo.Imagem;
import br.com.javamagazine.sfcb.modelo.Publicacao;
import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;
import br.com.javamagazine.sfcb.negocio.ServicoImagem;
import br.com.javamagazine.sfcb.negocio.ServicoImagensFB;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.EntityNotFoundException;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/22/13
 * Time: 2:55 AM
 * To change this template use File | Settings | File Templates.
 */
@Api(
        name = "sfcb",
        version = "v1",
        description = "API para a consulta de fotos e albums no Facebook"
)
public class FotoEndpoint {

    private static final Logger log = Logger.getLogger(FotoEndpoint.class.getName());

    @ApiMethod(
            name = "sfcb.foto.list",
            path = "foto",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Fotos listar(HttpServletRequest req, @Nullable @Named("limit") Integer limit) throws UnauthorizedException {
        final String accessToken = getAccessToken(req);
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken, limit);

        return imagensFB.listarTodas();
    }

    @ApiMethod(
            name = "sfcb.foto.pagina",
            path = "foto/cursor",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Fotos recuperarPagina(HttpServletRequest req, @Nullable @Named("pagina") String pagina) throws UnauthorizedException {
        final String accessToken = getAccessToken(req);
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);

        return imagensFB.buscarPagina(pagina);
    }

    @ApiMethod(
            name = "sfcb.foto.publicar",
            path = "foto",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Publicacao publicar(HttpServletRequest req, @Named String dataColagem) throws UnauthorizedException {
        final String accessToken = getAccessToken(req);

        log.info("Em Base64: " + dataColagem);

        final ServicoImagem servicoImagem = new ServicoImagem();
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);

        final Imagem imagem = servicoImagem.recuperarDaDataURL(dataColagem);

        log.info("MIME type: " + imagem.getMimeType());
        log.info("Extensao: " + imagem.getExtensao());


        Publicacao publicacao = null;
        try {
            publicacao = imagensFB.publicarRestFB(imagem);
            log.info("Retorno Facebook: " + publicacao);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Não foi possível fazer o upload para o Facebook", e);
        }

        return publicacao;
    }

    private String getAccessToken(HttpServletRequest req) throws UnauthorizedException {
        final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao();
        final String sfcbToken = req.getHeader("sfcb-token");
        log.info("SFCB Token: " + sfcbToken);
        final String accessToken;
        try {
            accessToken = servicoAutenticacao.getAccessToken(sfcbToken);
            log.info("Access Token: " + accessToken);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException("Falha de autenticacao", e);
        }

        return accessToken;
    }
}
