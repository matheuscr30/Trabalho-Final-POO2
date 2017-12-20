package com.matheus.magazinehenrique.cupons;

/**
 * Created by matheus on 17/12/17.
 */

public abstract class Cupom {

    protected Cupom next;
    protected IdCupom tipo;

    public Cupom(IdCupom tipo) {
        this.tipo = tipo;
        next = null;
    }

    public void setNext(Cupom cupom){
        if(next == null ){
            next = cupom;
        } else {
            next.setNext(cupom);
        }
    }

    public double aplicar(IdCupom id, double precoTotal) {

        if(verificaCupom(id)){
            return cupomAplicado(precoTotal);
        } else {
            if(next == null){
                return -1;
            }
            return next.aplicar(id, precoTotal);
        }

    }

    public boolean verificaCupom(IdCupom id){
        return id == tipo;
    }

    public abstract double cupomAplicado(double precoTotal);
}
