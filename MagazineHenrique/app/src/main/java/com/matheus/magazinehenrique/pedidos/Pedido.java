package com.matheus.magazinehenrique.pedidos;

import com.matheus.magazinehenrique.cupons.Cupom;
import com.matheus.magazinehenrique.cupons.IdCupom;
import com.matheus.magazinehenrique.fretes.Frete;

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

    public final boolean processar(IdCupom idCupom, double preco, int numParcelas) {
        precoTotal = preco;
        if (verificarEstoque() == false) {
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
        return true;
    }

    public final boolean finalizarCompra(){
        finalizarPedido();
        despachar();
        return true;
    }

    public final boolean verificarEstoque() {
        //if (estoqueSuficiente()) return true;
        //else return false;
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
