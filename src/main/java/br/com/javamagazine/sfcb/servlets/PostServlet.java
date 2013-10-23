package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.modelo.Imagem;
import br.com.javamagazine.sfcb.modelo.Publicacao;
import br.com.javamagazine.sfcb.negocio.ServicoImagem;
import br.com.javamagazine.sfcb.negocio.ServicoImagensFB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(PostServlet.class.getName());


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String accessToken = (String) request.getSession().getAttribute("accessToken");
        log.warning("Access Token: " + accessToken);
		final String imagemBase64 = request.getParameter("dataColagem");
        log.info("Em Base64: " + imagemBase64);

        final ServicoImagem servicoImagem = new ServicoImagem();
        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);

        final Imagem imagem = servicoImagem.recuperarDaDataURL(imagemBase64);

        log.info("MIME type: " + imagem.getMimeType());
        log.info("Extensao: " + imagem.getExtensao());


        final OutputStream out = response.getOutputStream();

        try {
            Publicacao publicacao = imagensFB.publicarGraphAPI(imagem);
            log.info("Retorno Facebook: " + publicacao);

            response.setContentType(imagem.getMimeType());
            out.write(imagem.getCorpo());
            // TODO: Ao inves de mostrar a imagem deve mostrar uma tela de sucesso com links para visualizar colagem
            // publicada e criar uma nova colagem

        } catch (Exception e) {
            log.log(Level.SEVERE, "Não foi possível fazer o upload para o Facebook", e);
            // TODO: Tratar e redirecionar o usuário para uma página de erro
        }

	}

}
