package br.com.javamagazine.sfcb.modelo;

import com.google.appengine.api.datastore.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/24/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Token implements Serializable {

    private final String UUID;
    private final String accessToken;
    private Date expiracao;


    public static Token fromEntity(Entity e) {
        String UUID = e.getKey().getName();
        String accessToken = (String) e.getProperty("accessToken");
        Date expiracao = (Date) e.getProperty("expireDate");
        return new Token(UUID, accessToken, expiracao);
    }

    public static Entity toEntity(Token t) {
        final Entity e = new Entity("Token", t.getUUID());
        e.setProperty("accessToken", t.getAccessToken());
        e.setProperty("expireDate", t.getExpiracao());

        return e;
    }

    private Token(String uuid, String accessToken, Date expiracao) {
        UUID = uuid;
        this.accessToken = accessToken;
        this.expiracao = expiracao;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpiracao() {
        return expiracao;
    }

    public void setExpiracao(Date expiracao) {
        this.expiracao = expiracao;
    }

    public String getUUID() {
        return UUID;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", UUID='" + UUID + '\'' +
                ", expiracao=" + expiracao +
                '}';
    }
}
