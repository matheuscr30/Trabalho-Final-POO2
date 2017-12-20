package com.matheus.magazinehenrique.cupons;

/**
 * Created by matheus on 17/12/17.
 */

public class CupomNatal extends Cupom{

    public CupomNatal() {
        super(IdCupom.CupomNatal);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.40;
    }
}
