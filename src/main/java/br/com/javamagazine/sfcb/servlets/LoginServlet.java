package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;
import com.restfb.FacebookClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LoginServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            // TODO: Autenticacao falhou, exibir mensagem
            response.sendRedirect("/index.jsp");
        }
		final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao(code);
		
		final FacebookClient.AccessToken token = servicoAutenticacao.getFacebookUserToken();
		final String accessToken = token.getAccessToken();
		final Date expires = token.getExpires();

        log.info("Access Token: " + accessToken);
        log.info("Token expires: " + expires);

        request.getSession().setAttribute("accessToken", accessToken);
        
        response.sendRedirect("/colagem");
		
	}
	
}
