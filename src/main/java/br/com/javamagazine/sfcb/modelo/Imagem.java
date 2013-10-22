package br.com.javamagazine.sfcb.modelo;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/21/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Imagem {

    private final byte[] corpo;
    private final String mimeType;

    public Imagem(byte[] corpo, String mimeType) {
        this.corpo = corpo;
        this.mimeType = mimeType;
    }

    public byte[] getCorpo() {
        return corpo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtensao() {
        // Para um mime type image/png volta png
        return mimeType != null ? mimeType.substring(Math.max(0, mimeType.length() - 3)) : "";
    }
}
