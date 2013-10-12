package br.com.javamagazine.sfcb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;

public class ImageProxy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String urlImagem = (String) request.getParameter("urlImagemFb");
		 
		InputStream input = new URL(urlImagem).openStream();
		OutputStream output = response.getOutputStream();
		
		response.setContentType("image/jpg");
		
		ByteStreams.copy(input, output);
		output.flush();
		output.close();
	}

}
