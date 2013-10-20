package br.com.javamagazine.sfcb.negocio;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/19/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageTransformer {

    private final ImagesService imagesService;

    public ImageTransformer() {
        imagesService = ImagesServiceFactory.getImagesService();
    }

    public byte[] resizeImage(byte[] original) {
        Image oldImage = ImagesServiceFactory.makeImage(original);
        Transform resize = ImagesServiceFactory.makeResize(600, 600, false);

        Image newImage = imagesService.applyTransform(resize, oldImage);

        return newImage.getImageData();
    }
}
