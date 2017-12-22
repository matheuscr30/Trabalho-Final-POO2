package com.matheus.magazinehenrique.estoque;

import com.matheus.magazinehenrique.model.Produto;

/**
 * Created by matheus on 21/12/17.
 */

public class StateAcabando extends StateEstoque {
    public StateAcabando(Produto produto) {
        super(produto);
    }

    @Override
    public void retiraDoEstoque(int qnt) {
        int qntEstoque = produto.getQuantidadeEstoque();
        int diff = qntEstoque - qnt;

        if (diff == 0) {
            produto.setStateEstoque(new StateEsgotado(produto));
            produto.setStatus("Esgotado");
        }

        produto.setQuantidadeEstoque(diff);
    }

    @Override
    public void adicionaNoEstoque(int qnt) {
        int qntEstoque = produto.getQuantidadeEstoque();
        int after = qntEstoque + qnt;

        if(after > faixaAcabando){
            produto.setStateEstoque(new StateDisponivel(produto));
            produto.setStatus("Disponível");
        }

        produto.setQuantidadeEstoque(after);
    }
}
