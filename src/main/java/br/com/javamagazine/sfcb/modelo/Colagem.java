package br.com.javamagazine.sfcb.modelo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 11/17/13
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Colagem implements Serializable {

    private String dataURL;

    public Colagem() {
    }

    public String getDataURL() {
        return dataURL;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }
}
