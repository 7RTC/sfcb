package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.negocio.ServicoAutenticacao;
import com.restfb.FacebookClient;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
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

        // insere token na sessao
        final HttpSession session = request.getSession();
        session.setAttribute("accessToken", accessToken);
        // gera um uuid para ser trocado pelo token para a camada de rest
        final String sfcbToken = UUID.randomUUID().toString();
        session.setAttribute("sfcbToken", sfcbToken);


        try {
            final CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            final Cache cache = cacheFactory.createCache(Collections.emptyMap());
            CacheManager.getInstance().registerCache("sfcbCache", cache);
            // Associa tokens
            cache.put(sfcbToken, accessToken);
        } catch (CacheException e) {
            log.log(Level.SEVERE, "Erros colocando o access_token o cache", e);
            request.getSession().invalidate();
            response.sendRedirect("/login");
        }

        response.sendRedirect("/colagem");
		
	}
	
}
