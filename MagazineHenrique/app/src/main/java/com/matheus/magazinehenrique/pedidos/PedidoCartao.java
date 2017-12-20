package com.matheus.magazinehenrique.pedidos;

import com.matheus.magazinehenrique.cupons.Cupom;
import com.matheus.magazinehenrique.fretes.Frete;

/**
 * Created by matheus on 17/12/17.
 */

public class PedidoCartao extends Pedido {
    public PedidoCartao(Cupom cupom, Frete frete) {
        super(cupom, frete);
    }

    @Override
    public double operacaoTipoPagamento() {
        return precoTotal*0.015;
    }

    @Override
    public void compraParcelada(int numParcelas) {
        if(numParcelas >= 3){
            precoTotal += precoTotal*0.05;
        }
    }
}
