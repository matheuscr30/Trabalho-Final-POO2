package com.matheus.magazinehenrique.cupons;


public class CupomBermuda extends Cupom {

    public CupomBermuda() {
        super(IdCupom.CupomBermuda);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.25;
    }
}