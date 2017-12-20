package com.matheus.magazinehenrique.fretes;

/**
 * Created by matheus on 17/12/17.
 */

public class FretePAC extends Frete{
    @Override
    public double calculaPreco() {
        return 15.00;
    }

    @Override
    public int calculaDiasUteis() {
        return 15;
    }
}
