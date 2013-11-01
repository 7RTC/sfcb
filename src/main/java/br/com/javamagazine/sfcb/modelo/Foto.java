package br.com.javamagazine.sfcb.modelo;

import com.restfb.types.Photo;

import java.io.Serializable;

public class Foto implements Serializable {

    private String picture;
    private String source;
    private String proxyURL;

    public Foto() {
    }

    public Foto(Photo photo) {
        this(photo.getPicture(), "/imageProxy?urlImagemFb=" + photo.getSource() , photo.getSource());
    }

    public Foto(String picture, String proxyURL, String source) {
        this.picture = picture;
        this.proxyURL = proxyURL;
        this.source = source;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProxyURL() {
        return proxyURL;
    }

    public void setProxyURL(String proxyURL) {
        this.proxyURL = proxyURL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
