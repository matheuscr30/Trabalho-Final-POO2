package com.matheus.magazinehenrique.cupons;


public class CupomCalca extends Cupom {

    public CupomCalca() {
        super(IdCupom.CupomCalca);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.15;
    }
}