package br.com.javamagazine.sfcb;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.restfb.FacebookClient;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String code = request.getParameter("code");
		String redirectUrl = "http://jm-sfcb.appspot.com:8080/Login";
		
		LoginFacebookClient loginFacebookClient = new LoginFacebookClient();
		
		FacebookClient.AccessToken token = loginFacebookClient.getFacebookUserToken(code, redirectUrl);
		String accessToken = token.getAccessToken();
		Date expires = token.getExpires();
		System.out.println(accessToken);
		System.out.println(expires.getTime());
		
        HttpSession session = request.getSession(true);
        session.setAttribute("accessToken", accessToken);
        
        request.getRequestDispatcher("/Colagem").forward(request, response);
		
	}
	
}
