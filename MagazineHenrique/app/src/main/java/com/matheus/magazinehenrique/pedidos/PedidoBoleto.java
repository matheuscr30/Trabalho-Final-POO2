package com.matheus.magazinehenrique.pedidos;

import com.matheus.magazinehenrique.cupons.Cupom;
import com.matheus.magazinehenrique.fretes.Frete;

/**
 * Created by matheus on 17/12/17.
 */

public class PedidoBoleto extends Pedido{
    public PedidoBoleto(Cupom cupom, Frete frete) {
        super(cupom, frete);
    }

    @Override
    public double operacaoTipoPagamento() {
        return -1*precoTotal*0.05;
    }
}
