package br.com.javamagazine.sfcb.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.javamagazine.sfcb.modelo.Token;
import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;

import com.restfb.FacebookClient;

public class LoginServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LoginServlet.class.getName());


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {


        final String accessTokenGeradoSDK = request.getParameter("accessToken");
        final String tempoExpiracaoGeradoSDK = request.getParameter("expiresIn");
        final String userIDGeradoSDK = request.getParameter("userID");

        if (accessTokenGeradoSDK == null || accessTokenGeradoSDK.isEmpty()) {
            log.info("login inválido");
            // TODO: Autenticacao falhou, exibir mensagem
            response.sendRedirect("/index.jsp");
        }

        {
            final int seconds = Integer.parseInt(tempoExpiracaoGeradoSDK);
            final Calendar expires = Calendar.getInstance();
            expires.add(Calendar.SECOND, seconds);
            log.info("Access Token SDK: " + accessTokenGeradoSDK);
            log.info("Expiração Token SDK: " + expires.getTime());
        }

        final ServicoAutenticacao servicoAutenticacao = new ServicoAutenticacao();

        final FacebookClient.AccessToken token = servicoAutenticacao.extendUserToken(accessTokenGeradoSDK);

        final Token tokenEstendido = servicoAutenticacao.storeInMemcache(token, userIDGeradoSDK);

        log.info("Access Token estendido: " + tokenEstendido.getAccessToken());
        log.info("Expiração Token estendido: " + tokenEstendido.getExpiracao());

        // insere token na sessao
        final HttpSession session = request.getSession();
        session.setAttribute("accessToken", tokenEstendido.getAccessToken());
        session.setAttribute("tokenUUID", tokenEstendido.getUUID());

        log.info("Redirecionando");
        response.sendRedirect("/colagem");
    }


}
