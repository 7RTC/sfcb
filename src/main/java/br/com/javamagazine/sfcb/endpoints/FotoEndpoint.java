package br.com.javamagazine.sfcb.endpoints;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.javamagazine.sfcb.modelo.*;
import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;
import br.com.javamagazine.sfcb.negocio.ServicoImagem;
import br.com.javamagazine.sfcb.negocio.ServicoImagensFB;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;

@Api(
        name = "sfcb",
        version = "v1",
        description = "API para a consulta de fotos e albums no Facebook"
)
public class FotoEndpoint {

    private static final Logger log = Logger.getLogger(FotoEndpoint.class.getName());
    private final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao();

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
    		name = "sfcb.album.foto.list",
    		path = "album/{id}",
    		httpMethod = ApiMethod.HttpMethod.GET
    		)
    public Fotos recuperarAlbum(HttpServletRequest req, @Named("id") long albumId, @Nullable @Named("limit") Integer limit) throws UnauthorizedException {
    	final String accessToken = getAccessToken(req);
    	final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken, limit);
    	
    	return imagensFB.listarAlbum(albumId);
    }

    @ApiMethod(
            name = "sfcb.foto.pagina",
            path = "foto/cursor",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Fotos recuperarPagina(HttpServletRequest req, @Nullable @Named("pagina") String pagina) throws UnauthorizedException {
        final String accessToken = getAccessToken(req);
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);

        log.info("Página: " + pagina);
        
        return imagensFB.buscarPagina(pagina);
    }

    @ApiMethod(
            name = "sfcb.foto.publicar",
            path = "foto",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Publicacao publicar(HttpServletRequest req, Colagem colagem) throws UnauthorizedException {
        final String accessToken = getAccessToken(req);

        log.info("Em Base64: " + colagem.getDataURL());

        final ServicoImagem servicoImagem = new ServicoImagem();
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);

        final Imagem imagem = servicoImagem.recuperarDaDataURL(colagem.getDataURL());

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
    	final String tokenUUID = req.getHeader("token-uuid");
    	final Token token = servicoAutenticacao.getFromMemcache(tokenUUID);
        if (tokenUUID == null) {
    		throw new UnauthorizedException("UUID não consta no header");
    	} else if (token == null) {
            throw new UnauthorizedException("UUID invalido ou expirado");
        }

        log.info("Token recuperado do cache: " + token);

        return token.getAccessToken();
    }
}
