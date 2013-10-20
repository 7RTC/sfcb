package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.negocio.LoginFacebookClient;
import com.restfb.FacebookClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final String code = request.getParameter("code");
		final LoginFacebookClient loginFacebookClient = new LoginFacebookClient(code);
		
		FacebookClient.AccessToken token = loginFacebookClient.getFacebookUserToken();
		String accessToken = token.getAccessToken();
		Date expires = token.getExpires();

        HttpSession session = request.getSession(true);
        session.setAttribute("accessToken", accessToken);
        
        response.sendRedirect("/Colagem");
		
	}
	
}
