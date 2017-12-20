package com.matheus.magazinehenrique.cupons;

/**
 * Created by matheus on 17/12/17.
 */

public class CupomAnoNovo extends Cupom{

    public CupomAnoNovo() {
        super(IdCupom.CupomAnoNovo);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.35;
    }
}
