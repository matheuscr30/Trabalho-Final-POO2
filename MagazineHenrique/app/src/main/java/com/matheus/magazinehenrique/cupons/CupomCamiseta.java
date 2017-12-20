package com.matheus.magazinehenrique.cupons;


public class CupomCamiseta extends Cupom {

    public CupomCamiseta() {
        super(IdCupom.CupomCamiseta);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.05;
    }
}