package br.com.javamagazine.sfcb;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/5/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookServlet extends HttpServlet {

	private static final long serialVersionUID = 1962734670711460780L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        FacebookClient facebookClient = new DefaultFacebookClient("INCLUIR TOKEN");
        response.setContentType("text/plain");
        final PrintWriter writer = response.getWriter();

        JsonObject photosConnection = facebookClient.fetchObject("me/photos", JsonObject.class);

        JsonArray data = photosConnection.getJsonArray("data");
        for (int i = 0; i < data.length(); i++) {
            final JsonObject jsonObject = data.getJsonObject(i);
            writer.println(jsonObject.getString("source"));
        }

        writer.close();

    }
}
