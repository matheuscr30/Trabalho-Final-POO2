package com.matheus.magazinehenrique.tools;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by matheus on 17/10/17.
 */

public class RepoCEP{
    @SerializedName("cep")
    String CEP;

    @SerializedName("logradouro")
    String Rua;

    @SerializedName("localidade")
    String Cidade;

    @SerializedName("uf")
    String Estado;

    public RepoCEP(String CEP, String rua, String cidade, String estado) {
        this.CEP = CEP;
        Rua = rua;
        Cidade = cidade;
        Estado = estado;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String rua) {
        Rua = rua;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
