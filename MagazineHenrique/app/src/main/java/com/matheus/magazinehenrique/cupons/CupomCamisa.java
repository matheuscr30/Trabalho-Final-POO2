package com.matheus.magazinehenrique.cupons;


public class CupomCamisa extends Cupom {
    public CupomCamisa() {
        super(IdCupom.CupomCamisa);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.17;
    }
}