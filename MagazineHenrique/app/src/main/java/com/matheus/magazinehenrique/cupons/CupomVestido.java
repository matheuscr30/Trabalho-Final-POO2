package com.matheus.magazinehenrique.cupons;


public class CupomVestido extends Cupom {
    public CupomVestido() {
        super(IdCupom.CupomVestido);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.9;
    }
}