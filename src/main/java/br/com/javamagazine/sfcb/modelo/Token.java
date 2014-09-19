package br.com.javamagazine.sfcb.modelo;

import com.google.appengine.api.datastore.Entity;
import com.restfb.FacebookClient;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String UUID;
    private final String accessToken;
    private final String userId;
    private Date expiracao;

    public static Token fromFBToken(FacebookClient.AccessToken fbToken, String userId, String UUID) {
        return new Token(UUID, fbToken.getAccessToken(), userId, fbToken.getExpires());
    }


    public static Token fromEntity(Entity e) {
        String userId = e.getKey().getName();
        String accessToken = (String) e.getProperty("accessToken");
        Date expiracao = (Date) e.getProperty("expireDate");
        String UUID = (String) e.getProperty("UUID");
        return new Token(UUID, accessToken, userId, expiracao);
    }

    public static Entity toEntity(Token t) {
        final Entity e = new Entity("Token", t.getUserId());
        e.setProperty("accessToken", t.getAccessToken());
        e.setProperty("expireDate", t.getExpiracao());
        e.setProperty("UUID", t.getUUID());

        return e;
    }

    private Token(String uuid, String accessToken, String userId, Date expiracao) {
        UUID = uuid;
        this.accessToken = accessToken;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", UUID='" + UUID + '\'' +
                ", userId='" + userId + '\'' +
                ", expiracao=" + expiracao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Token token = (Token) o;

        return !(userId != null ? !userId.equals(token.userId) : token.userId != null);

    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
