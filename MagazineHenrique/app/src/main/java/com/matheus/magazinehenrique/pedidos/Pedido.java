package com.matheus.magazinehenrique.pedidos;

import com.google.firebase.database.DatabaseReference;
import com.matheus.magazinehenrique.config.ConfiguracaoFirebase;
import com.matheus.magazinehenrique.cupons.Cupom;
import com.matheus.magazinehenrique.cupons.IdCupom;
import com.matheus.magazinehenrique.fretes.Frete;
import com.matheus.magazinehenrique.model.Produto;

import java.util.ArrayList;

/**
 * Created by matheus on 17/12/17.
 */

public abstract class Pedido {

    public Cupom cupom;
    public Frete frete;
    public double precoTotal;

    public Pedido(Cupom cupom, Frete frete) {
        this.cupom = cupom;
        this.frete = frete;
    }

    public final boolean processar(IdCupom idCupom, double preco, int numParcelas, ArrayList<Produto>produtos,
                                   ArrayList<String>idProdutos, ArrayList<Integer>qtdProdutos) {
        precoTotal = preco;
        if (verificarEstoque(produtos, idProdutos, qtdProdutos) == false) {
            System.out.println("ne possivel");
            return false;
        }

        double precoComDesconto = 0;
        if (idCupom != null) {
            precoComDesconto = calcularDesconto(idCupom, preco);
            if (precoComDesconto != -1) {
                precoTotal = precoComDesconto;
            }
        }

        precoTotal += calcularFrete();
        precoTotal += operacaoTipoPagamento();

        compraParcelada(numParcelas);
        finalizarCompra();
        return true;
    }

    public final boolean finalizarCompra(){
        finalizarPedido();
        despachar();
        return true;
    }

    public final boolean verificarEstoque(ArrayList<Produto>produtos, ArrayList<String>idProdutos,
                                          ArrayList<Integer>qtdProdutos) {

        for(int i = 0; i < produtos.size(); i++){
            if(produtos.get(i).getQuantidadeEstoque() < qtdProdutos.get(i)) {
                System.out.println(produtos.get(i).getQuantidadeEstoque() + " " + qtdProdutos.get(i));
                return false;
            }
        }

        for(int i = 0; i < produtos.size(); i++){
            produtos.get(i).retiraDoEstoque(qtdProdutos.get(i));
        }
        return true;
    }

    public void compraParcelada(int numParcelas){ }

    public double calcularDesconto(IdCupom idCupom, double preco) {
        return cupom.aplicar(idCupom, preco);
    }

    public double calcularFrete() {
        return frete.calculaPreco();
    }

    public void despachar() {
        System.out.println("Despachando Produto");
    }

    public abstract double operacaoTipoPagamento();

    public void finalizarPedido() {
        System.out.println("Pedido realizado com sucesso");
    }

    public Cupom getCupom() {
        return cupom;
    }

    public void setCupom(Cupom cupom) {
        this.cupom = cupom;
    }

    public Frete getFrete() {
        return frete;
    }

    public void setFrete(Frete frete) {
        this.frete = frete;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }
}
