package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.modelo.Imagem;
import br.com.javamagazine.sfcb.negocio.ServicoImagem;
import com.google.common.io.ByteStreams;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyServlet  extends HttpServlet {
    private static final Logger log = Logger.getLogger(ProxyServlet.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int connectTimeout = 25000;
    private static final int readTimeout = 60000;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final ServicoImagem servicoImagem = new ServicoImagem();

        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(request.getParameter("urlImagemFb")).openConnection();
            connection.setConnectTimeout(connectTimeout); // Timeouts
            connection.setReadTimeout(readTimeout);

            log.info("Headers do request");
            final Enumeration<String> headerNames = request.getHeaderNames();
            while(headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                final String headerValue = request.getHeader(headerName) ;
                log.info(headerName + " + " + headerValue);
              //  connection.setRequestProperty(headerName, headerValue);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Não foi possível recuperar a imagem", e);
            response.sendRedirect("/erro");
           throw e;
        }

        log.info("Headers da resposta");
        for(Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            log.info(entry.getKey() + " = " + entry.getValue());
        }

        log.info("Status code: " + connection.getResponseCode());

        // Copia cabecalhos para o proxy
        response.setStatus(connection.getResponseCode());
        response.setContentType(connection.getContentType());
        response.setContentLength(connection.getContentLength());
        response.setDateHeader("last-modified", connection.getHeaderFieldDate("last-modified", new Date().getTime()));
        response.setDateHeader("date", connection.getHeaderFieldDate("date", new Date().getTime()));
        response.setHeader("keep-alive", connection.getHeaderField("keep-alive"));
        // O facebook costuma manter imagens em cache por aproximadamente  14 dias
        response.setDateHeader("max-age", connection.getHeaderFieldDate("max-age", new Date().getTime()));

        final ServletOutputStream out = response.getOutputStream();

        try (InputStream input = connection.getInputStream()) {
            final String mimeType = connection.getContentType();
            final byte[] corpo = ByteStreams.toByteArray(input);

            final Imagem original = new Imagem(corpo, mimeType);
            final Imagem redimensionada = servicoImagem.redimensionar(original);
            out.write(redimensionada.getCorpo());
       } catch (Exception e) {
            log.log(Level.SEVERE, "Não foi possível tratar a imagem do facebook", e);
            response.sendRedirect("/erro");
        }
    }

}
