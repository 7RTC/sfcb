package br.com.javamagazine.sfcb.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;

import com.restfb.FacebookClient;

public class LoginServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

/*				
		final String code = request.getParameter("code");

		if (code == null || code.isEmpty()) {
			// TODO: Autenticacao falhou, exibir mensagem
			response.sendRedirect("/index.jsp");
		}

		final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao(code);

		final FacebookClient.AccessToken token = servicoAutenticacao.getFacebookUserToken();
*/
		
		final String accessTokenGeradoSDK = request.getParameter("accessToken");
		final String tempoExpiracaoGeradoSDK = request.getParameter("expiresIn");

		if (accessTokenGeradoSDK == null || accessTokenGeradoSDK.isEmpty()) {
			// TODO: Autenticacao falhou, exibir mensagem
			response.sendRedirect("/index.jsp");
		}

		final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao();


		final FacebookClient.AccessToken token = servicoAutenticacao.getFacebookUserToken(accessTokenGeradoSDK, tempoExpiracaoGeradoSDK);
		final String accessToken = token.getAccessToken();
		final Date expires = token.getExpires();

		log.info("FB Access Token: " + accessToken);
		log.info("Expiração Token: " + expires);

		// insere token na sessao
		final HttpSession session = request.getSession();
		session.setAttribute("accessToken", accessToken);

		response.sendRedirect("/colagem");
	}

}
