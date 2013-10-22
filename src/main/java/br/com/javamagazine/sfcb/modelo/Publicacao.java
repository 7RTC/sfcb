package br.com.javamagazine.sfcb.modelo;

import com.google.gson.annotations.SerializedName;
import com.restfb.Facebook;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/22/13
 * Time: 4:57 AM
 * To change this template use File | Settings | File Templates.
 */
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
