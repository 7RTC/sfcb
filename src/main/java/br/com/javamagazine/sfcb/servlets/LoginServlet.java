package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.modelo.Token;
import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;
import com.restfb.FacebookClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        log.info("FB Access Token: " + accessToken);
        log.info("Expiração Token: " + expires);

        // insere token na sessao
        final HttpSession session = request.getSession();

        // TODO: Buscar token por user id, não accessToken
        // recupera ou gera um token da aplicacao para ser trocado pelo token do facebook
        Token sfcbToken = servicoAutenticacao.getSFCBAccessToken(accessToken);
        if (sfcbToken == null) {
            log.info("Criando access Token");
            sfcbToken = servicoAutenticacao.createAccessToken(token);
        }
        else {
            // TODO: Validar token
        }
        if (sfcbToken.getExpiracao().before(expires)) {
            log.info("Atualizando data de expiracao");
            servicoAutenticacao.updateExpirationDate(sfcbToken, expires);
        }

        session.setAttribute("sfcbToken", sfcbToken.getUUID());
        session.setAttribute("accessToken", sfcbToken.getAccessToken());

        log.info("SFCB Token: " + sfcbToken);
        log.info("Data de expiracao confirma? " + expires.equals(sfcbToken.getExpiracao()));

        response.sendRedirect("/colagem");
    }

}
