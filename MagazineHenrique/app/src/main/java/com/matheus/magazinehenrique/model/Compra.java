package com.matheus.magazinehenrique.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by matheus on 20/12/17.
 */

public class Compra {
    ArrayList<String> idProdutos;
    ArrayList<Integer> qtdProdutos;
    Date data;
    String valorTotal;
    String chave;
    String status;

    public Compra() {

    }

    public ArrayList<String> getIdProdutos() {
        return idProdutos;
    }

    public void setIdProdutos(ArrayList<String> idProdutos) {
        this.idProdutos = idProdutos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public ArrayList<Integer> getQtdProdutos() {
        return qtdProdutos;
    }

    public void setQtdProdutos(ArrayList<Integer> qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
