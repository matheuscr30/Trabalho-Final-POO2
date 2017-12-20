package com.matheus.magazinehenrique.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by matheus on 15/12/17.
 */

public class Carrinho {

    private ArrayList<String>idProdutos;
    private ArrayList<Integer>qtdProdutos;

    public Carrinho() {

    }

    public void addProduto(String idProduto){

        int indice = idProdutos.indexOf(idProduto);

        if(indice == -1){
            idProdutos.add(idProduto);
            qtdProdutos.add(1);
        } else {
            qtdProdutos.set(indice, qtdProdutos.get(indice)+1);
        }
    }

    public ArrayList<String> getIdProdutos() {
        return idProdutos;
    }

    public void setIdProdutos(ArrayList<String> idProdutos) {
        this.idProdutos = idProdutos;
    }

    public ArrayList<Integer> getQtdProdutos() {
        return qtdProdutos;
    }

    public void setQtdProdutos(ArrayList<Integer> qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }
}
