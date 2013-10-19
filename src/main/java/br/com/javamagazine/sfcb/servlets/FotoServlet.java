package br.com.javamagazine.sfcb.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.StyledEditorKit.ItalicAction;

import br.com.javamagazine.sfcb.modelo.Foto;
import br.com.javamagazine.sfcb.modelo.Fotos;
import com.google.gson.Gson;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Photo;
import com.restfb.types.Post;

/**
 * Servlet implementation class FotoServlet
 */
public class FotoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(FotoServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        final String accessToken = (String) session.getAttribute("accessToken");
        System.out.println(accessToken);


        FacebookClient facebookClient = new DefaultFacebookClient(accessToken);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JsonObject photosConnection = facebookClient.fetchObject("me/photos?", JsonObject.class, Parameter.with("fields", "source, picture"), Parameter.with("type", "uploaded"));
        System.out.println(photosConnection.toString());

//		Connection<Photo> connection = facebookClient.fetchConnection("me/photos", Photo.class, Parameter.with("limit", 10), Parameter.with("type", "uploaded"));
//
//
//		//TODO Usar para paginação
//		if (connection.hasNext()) {
//			int count = 0;
//			Iterator<List<Photo>> iterator = connection.iterator();
//			while (iterator.hasNext()) {
//				System.out.println("Pagina:" + ++count);
//				List<Photo> photos = iterator.next();
//				for (Photo photo : photos) {
//					String picStr = photo.getPicture();
//					System.out.println(picStr);
//				}
//			}
//			
//			System.out.println(connection.getNextPageUrl());
//		}


        JsonArray data = photosConnection.getJsonArray("data");
        Fotos fotos = new Fotos();

        for (int i = 0; i < data.length(); i++) {
            final JsonObject jsonObject = data.getJsonObject(i);
            Foto foto = new Foto();

            foto.setPicture(jsonObject.getString("picture").trim());
            foto.setSource(request.getRequestURL() + "/ImageProxy?urlImagemFb=" + jsonObject.getString("source").trim());
            fotos.getFotos().add(foto);
        }

        Gson gson = new Gson();
        String jsonStr = gson.toJson(fotos);
        log.info(jsonStr);

        out.print(jsonStr);
        out.flush();
        out.close();

    }

}