package com.matheus.magazinehenrique.estoque;

import com.google.gson.annotations.SerializedName;
import com.matheus.magazinehenrique.model.Produto;

import java.io.Serializable;

public class StateDisponivel extends StateEstoque{
    public StateDisponivel(Produto produto) {
        super(produto);
    }

    @Override
    public void retiraDoEstoque(int qnt) {
        int qntEstoque = produto.getQuantidadeEstoque();
        int diff = qntEstoque - qnt;

        if (diff == 0) {
            produto.setStateEstoque(new StateEsgotado(produto));
            produto.setStatus("Esgotado");
        } else if (diff <= faixaAcabando) {
            produto.setStateEstoque(new StateAcabando(produto));
            produto.setStatus("Acabando");
        }

        produto.setQuantidadeEstoque(diff);
        System.out.println(produto.getNome() + " " + produto.getStatus());
    }

    @Override
    public void adicionaNoEstoque(int qnt) {
        int after = produto.getQuantidadeEstoque() + qnt;
        produto.setQuantidadeEstoque(after);
    }

}
