package com.matheus.magazinehenrique.estoque;

import com.matheus.magazinehenrique.model.Produto;

import java.io.Serializable;

/**
 * Created by matheus on 21/12/17.
 */

public abstract class StateEstoque implements Serializable{

    protected Produto produto;
    final int faixaAcabando = 4;

    public StateEstoque(Produto produto) {
        this.produto = produto;
    }

    public abstract void retiraDoEstoque(int qnt);

    public abstract void adicionaNoEstoque(int qnt);
}
