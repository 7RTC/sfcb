package br.com.javamagazine.sfcb.modelo;

import com.google.gson.annotations.SerializedName;
import com.restfb.Facebook;

public class Publicacao {

    @Facebook
    private String id;

    @SerializedName("post_id")
    @Facebook("post_id")
    private String postId;

    public Publicacao() {
    }

    public Publicacao(String id, String postId) {
        this.id = id;
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Publicacao{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
