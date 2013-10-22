package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.modelo.Fotos;
import br.com.javamagazine.sfcb.negocio.ServicoImagensFB;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Servlet implementation class FotoServlet
 */
public class FotoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(FotoServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String accessToken = (String) request.getSession().getAttribute("accessToken");
        final String pagina = request.getParameter("pagina");
        log.info("Access Token: " + accessToken);

        final ServicoImagensFB imagensFB = new ServicoImagensFB(accessToken);
        final Gson gson = new Gson();

        response.setContentType("application/json");
        final PrintWriter out = response.getWriter();

        final Fotos fotos = imagensFB.listarTodas();
        final String fotosJson = gson.toJson(fotos);
        out.print(fotosJson);

    }

}