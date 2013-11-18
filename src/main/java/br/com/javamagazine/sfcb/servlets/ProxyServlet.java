package br.com.javamagazine.sfcb.servlets;

import br.com.javamagazine.sfcb.modelo.Imagem;
import br.com.javamagazine.sfcb.negocio.ServicoImagem;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ProxyServlet.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int connectTimeout = 25000;
    private static final int readTimeout = 60000;
    public static final ImmutableSet<String> excludedHeaders = ImmutableSet.of("Cookie", "Host");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final ServicoImagem servicoImagem = new ServicoImagem();

        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(request.getParameter("urlImagemFb")).openConnection();
            connection.setConnectTimeout(connectTimeout); // Timeouts
            connection.setReadTimeout(readTimeout);

            log.info("Headers do request");
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                final String headerValue = request.getHeader(headerName);

                if (!excludedHeaders.contains(headerName)) {
                    log.info(headerName + " : " + headerValue);
                    connection.setRequestProperty(headerName, headerValue);
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Não foi possível recuperar a imagem", e);
            response.sendRedirect("/erro");
            throw e;
        }

        final int httpCode = connection.getResponseCode();
        response.setStatus(httpCode);

        log.info("Status code: " + httpCode);

        log.info("Headers da resposta");
        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            final String header = entry.getKey();
            log.info(entry.getKey() + " = " + entry.getValue());
            for (String headerValue : entry.getValue()) {
                response.addHeader(header, headerValue);
            }
        }

        final ServletOutputStream out = response.getOutputStream();

        try (InputStream input = connection.getInputStream()) {
            final String mimeType = connection.getContentType();
            final byte[] corpo = ByteStreams.toByteArray(input);

            if (httpCode == HttpServletResponse.SC_OK) {
                final Imagem original = new Imagem(corpo, mimeType);
                final Imagem redimensionada = servicoImagem.redimensionar(original);
                out.write(redimensionada.getCorpo());
            } else {
                out.write(corpo);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Não foi possível tratar a imagem do facebook", e);
            response.sendRedirect("/erro");
        }
    }

}
