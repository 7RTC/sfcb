package br.com.javamagazine.sfcb.modelo;

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
        return mimeType != null ? mimeType.substring(Math.min(6, mimeType.length())) : "";
    }
}
