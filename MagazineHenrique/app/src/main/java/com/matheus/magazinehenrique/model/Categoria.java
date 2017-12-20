package com.matheus.magazinehenrique.model;

import java.io.Serializable;

/**
 * Created by matheus on 15/12/17.
 */

public class Categoria implements Serializable{

    private String nome;
    private String nome_icone;

    public Categoria(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_icone() {
        return nome_icone;
    }

    public void setNome_icone(String nome_icone) {
        this.nome_icone = nome_icone;
    }
}
