package br.com.javamagazine.sfcb.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.javamagazine.sfcb.negocio.ImageTransformer;
import com.google.common.io.ByteStreams;

public class ProxyServlet  extends HttpServlet {
    private static final Logger log = Logger.getLogger(ProxyServlet.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int connectTimeout = 25000;
    private static final int readTimeout = 60000;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final URLConnection url = new URL(request.getParameter("urlImagemFb")).openConnection();
        url.setConnectTimeout(connectTimeout); // Timeouts
        url.setReadTimeout(readTimeout);

        log.info("Proxy headers");
        for(Map.Entry<String, List<String>> entry : url.getHeaderFields().entrySet()) {
            log.info(entry.getKey() + " = " + entry.getValue());
        }

        // Headers
        response.setContentType(url.getContentType()); // seta headers
        response.setContentLength(url.getContentLength());
        response.setDateHeader("last-modified", url.getHeaderFieldDate("last-modified", new Date().getTime()));
        response.setDateHeader("date", url.getHeaderFieldDate("date", new Date().getTime()));
        response.setHeader("keep-alive", url.getHeaderField("keep-alive"));
        // O facebook costuma manter imagens em cache por aproximadamente  14 dias
        response.setDateHeader("max-age", url.getHeaderFieldDate("max-age", new Date().getTime()));

        try (InputStream input = url.getInputStream(); OutputStream out = response.getOutputStream()) {
            final byte[] original = ByteStreams.toByteArray(input);
            final ImageTransformer it = new ImageTransformer();
            byte[] resized = it.resizeImage(original);
            out.write(resized);
       }
    }

}
