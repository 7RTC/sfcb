package br.com.javamagazine.sfcb.modelo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: a.accioly
 * Date: 10/24/13
 * Time: 1:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Usuario implements Serializable {
    private final long id;
    private final String name;

    public Usuario(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
