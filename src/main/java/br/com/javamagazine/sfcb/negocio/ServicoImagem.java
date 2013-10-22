package br.com.javamagazine.sfcb.negocio;

import br.com.javamagazine.sfcb.modelo.Imagem;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.apache.commons.codec.binary.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/19/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServicoImagem {

    private final ImagesService imagesService;

    public ServicoImagem() {
        imagesService = ImagesServiceFactory.getImagesService();
    }

    public Imagem recuperarDaDataURL(String dataURL) {
        // Decoda o string da imagem do canvas
        // Extrai image/tipo de uma string data:image/png|;base64
        final String mimeType = dataURL.substring(5, 14);
        // Descarta o mimeType e decoda o corpo da imagem
        byte[] corpo = Base64.decodeBase64(dataURL.split("^data:image/(png|jpg);base64,")[1]);

        return new Imagem(corpo, mimeType);
    }

    public Imagem redimensionar(Imagem original) {
        final Image originalGAE = ImagesServiceFactory.makeImage(original.getCorpo());
        /*
         * False faz o GAE manter a proporcao, logo a imagem serah redimensionada para o maior tamanho possivel que
         * caiba em um quadrado de 600 por 600
         */
        final Transform transformacao = ImagesServiceFactory.makeResize(600, 600, false);

        final Image novaGAE = imagesService.applyTransform(transformacao, originalGAE);

        return new Imagem(novaGAE.getImageData(), "image/" + novaGAE.getFormat().name().toLowerCase());
    }
}
