package br.com.javamagazine.sfcb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyledEditorKit.ItalicAction;

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		FacebookClient facebookClient = new DefaultFacebookClient("INCLUIR TOKEN");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		JsonObject photosConnection = facebookClient.fetchObject("me/photos", JsonObject.class);
		System.out.println(photosConnection.toString());

		Connection<Photo> connection = facebookClient.fetchConnection("me/photos", Photo.class, Parameter.with("limit", 10));

		
		if (connection.hasNext()) {
			int count = 0;
			Iterator<List<Photo>> iterator = connection.iterator();
			while (iterator.hasNext()) {
				System.out.println("Pagina:" + ++count);
				List<Photo> photos = iterator.next();
				for (Photo photo : photos) {
					String picStr = photo.getPicture();
					System.out.println(picStr);
				}
			}
			
			System.out.println(connection.getNextPageUrl());
		}
		
		
		
		
		
		JsonArray data = photosConnection.getJsonArray("data");

		StringBuilder sb = new StringBuilder();
		sb.append("{ \"fotos\": ");
		sb.append("[ ");

		for (int i = 0; i < data.length(); i++) {
			final JsonObject jsonObject = data.getJsonObject(i);
			sb.append("{  \"url\": ");
			sb.append("\""+request.getRequestURL()+"/ImageProxy?urlImagemFb="+jsonObject.getString("source").trim()+"\"");
			sb.append(" }");
			if (i < data.length() - 1) {
				sb.append(", ");
			}
			
		}
		sb.append(" ]");
		sb.append(" }");

		String jsonObject = sb.toString();

		out.print(jsonObject);
		out.flush();
		out.close();
		
	}

}