package com.matheus.magazinehenrique.estoque;

import com.matheus.magazinehenrique.model.Produto;

/**
 * Created by matheus on 21/12/17.
 */

public class StateEsgotado extends StateEstoque {
    public StateEsgotado(Produto produto) {
        super(produto);
    }

    @Override
    public void retiraDoEstoque(int qnt) {
        //Nao muda
    }

    @Override
    public void adicionaNoEstoque(int qnt) {
        int qntEstoque = produto.getQuantidadeEstoque();
        int after = qntEstoque + qnt;

        if (after <= faixaAcabando) {
            produto.setStateEstoque(new StateAcabando(produto));
            produto.setStatus("Acabando");
        } else if(after > faixaAcabando){
            produto.setStateEstoque(new StateDisponivel(produto));
            produto.setStatus("Disponível");
        }

        produto.setQuantidadeEstoque(after);
    }


}
