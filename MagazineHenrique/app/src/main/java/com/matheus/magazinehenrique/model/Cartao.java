package com.matheus.magazinehenrique.model;

/**
 * Created by matheus on 18/12/17.
 */

public class Cartao {

    private String NumeroCartao;
    private int Mes;
    private int Ano;
    private int CodigoSeguranca;
    private String Nome;
    private String Sobrenome;
    private String CPF;
    private int numParcelas;

    public Cartao(String numeroCartao, int mes, int ano, int codigoSeguranca, String nome, String sobrenome, String CPF, int numParcelas) {
        NumeroCartao = numeroCartao;
        Mes = mes;
        Ano = ano;
        CodigoSeguranca = codigoSeguranca;
        Nome = nome;
        Sobrenome = sobrenome;
        this.CPF = CPF;
        this.numParcelas = numParcelas;
    }

    public String getNumeroCartao() {
        return NumeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        NumeroCartao = numeroCartao;
    }

    public int getMes() {
        return Mes;
    }

    public void setMes(int mes) {
        Mes = mes;
    }

    public int getAno() {
        return Ano;
    }

    public void setAno(int ano) {
        Ano = ano;
    }

    public int getCodigoSeguranca() {
        return CodigoSeguranca;
    }

    public void setCodigoSeguranca(int codigoSeguranca) {
        CodigoSeguranca = codigoSeguranca;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getSobrenome() {
        return Sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        Sobrenome = sobrenome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public int getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(int numParcelas) {
        this.numParcelas = numParcelas;
    }
}
