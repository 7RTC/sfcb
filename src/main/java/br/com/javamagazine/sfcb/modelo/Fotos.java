package br.com.javamagazine.sfcb.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anthony Accioly
 * Date: 19/10/13
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class Fotos {

    private List<Foto> fotos;
    private String proximaPagina;
    private String paginaAnterior;
    private int count = 100;

	public Fotos () {
        this.fotos = new ArrayList<Foto>();
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getProximaPagina() {
        return proximaPagina;
    }

    public void setProximaPagina(String proximaPagina) {
        this.proximaPagina = proximaPagina;
    }
    
    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
