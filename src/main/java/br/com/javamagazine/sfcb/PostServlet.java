package br.com.javamagazine.sfcb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.common.io.ByteStreams;
import com.restfb.BinaryAttachment;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//
//		final GcsService gcsService =
//				GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
//
//		BlobKey blobKey = null;

		String imagemBase64 = request.getParameter("stringBase64Img");

		// Decoda o string da imagem do canvas
		byte[] decodedBytes = Base64.decodeBase64(imagemBase64.split("^data:image/(png|jpg);base64,")[1]);
		InputStream input = new ByteArrayInputStream(decodedBytes);
		
//		OutputStream output = response.getOutputStream();
		
//		//Teste decode (funcionando)
//		response.setContentType("image/jpg"); 
//		ByteStreams.copy(input, output);
//		output.flush();
//		output.close();

		
        HttpSession session = request.getSession(true);
        String accessToken = (String) session.getAttribute("accessToken");
		
		LoginFacebookClient loginFacebookClient = new LoginFacebookClient(accessToken);
		
		try {
			String nomeArquivo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			FacebookType publishPhotoResponse = loginFacebookClient.publish("me/photos", FacebookType.class,
					BinaryAttachment.with(nomeArquivo, input),
					Parameter.with("message", "Test"));

			System.out.println("Published photo ID: " + publishPhotoResponse.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		OutputStream output = response.getOutputStream();
		response.setContentType("image/png"); 
		ByteStreams.copy(input, output);
		output.flush();
		output.close();
		

//		// Salva objeto
//		String objName = String.valueOf(Calendar.getInstance().getTimeInMillis());
//		System.out.println(objName);
//		GcsFilename fileName = new GcsFilename("bucket", objName);
//		GcsOutputChannel gscOutputChannel = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
//		ByteStreams.copy(input, Channels.newOutputStream(gscOutputChannel));
//		gscOutputChannel.close();
//
//		GcsInputChannel gcsInputChannel = gcsService.openReadChannel(fileName, 0);
//		
//		ByteStreams.copy(gcsInputChannel, Channels.newChannel(output));
//		response.setContentType("image/png"); 
//		output.close();
		
//		// Recupera Blobkey
//		blobKey = blobstoreService.createGsBlobKey(
//				"/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
//		// Retorna imagem
//		ImagesService imagesService = ImagesServiceFactory.getImagesService();
//		ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
//		//blobstoreService.serve(blobKey, response);
//		String servingUrlImg = imagesService.getServingUrl(servingUrlOptions);
//		System.out.println(servingUrlImg);


	}

}
