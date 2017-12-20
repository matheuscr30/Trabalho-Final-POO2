package com.matheus.magazinehenrique.cupons;

public class CupomSaia extends Cupom {
    public CupomSaia() {
        super(IdCupom.CupomSaia);
    }

    @Override
    public double cupomAplicado(double precoTotal) {
        return precoTotal - precoTotal * 0.10;
    }
}