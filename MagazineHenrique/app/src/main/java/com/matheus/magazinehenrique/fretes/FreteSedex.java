package com.matheus.magazinehenrique.fretes;

/**
 * Created by matheus on 17/12/17.
 */

public class FreteSedex extends Frete{
    @Override
    public double calculaPreco() {
        return 30.00;
    }

    @Override
    public int calculaDiasUteis() {
        return 5;
    }
}
