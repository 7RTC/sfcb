package br.com.javamagazine.sfcb.modelo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Anthony Accioly
 * Date: 19/10/13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Foto implements Serializable {

    private String source;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
