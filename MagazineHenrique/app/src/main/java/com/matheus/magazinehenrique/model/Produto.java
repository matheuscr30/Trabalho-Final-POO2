package com.matheus.magazinehenrique.model;

import com.google.firebase.database.Exclude;
import com.matheus.magazinehenrique.estoque.StateAcabando;
import com.matheus.magazinehenrique.estoque.StateDisponivel;
import com.matheus.magazinehenrique.estoque.StateEsgotado;
import com.matheus.magazinehenrique.estoque.StateEstoque;

import java.io.Serializable;

/**
 * Created by matheus on 15/12/17.
 */

public class Produto implements Serializable{

    private String referencia;
    private String nome;
    private String descricao;
    private String cor;
    private String tipo;
    private String genero;
    private String tamanho;
    private String preco;
    private int quantidadeEstoque;
    private String status;
    @Exclude
    private StateEstoque stateEstoque;

    public Produto() {

    }

    @Exclude
    public void iniciaProduto(){
        if(status.equals("Disponível")){
            stateEstoque = new StateDisponivel(this);
        } else if(status.equals("Esgotado")){
            stateEstoque = new StateEsgotado(this);
        } else if(status.equals("Acabando")){
            stateEstoque = new StateAcabando(this);
        }
    }

    @Exclude
    public void retiraDoEstoque(int qtd) {
        stateEstoque.retiraDoEstoque(qtd);
    }

    @Exclude
    public void adicionaNoEstoque(int qtd) {
        stateEstoque.adicionaNoEstoque(qtd);
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    public StateEstoque getStateEstoque() {
        return stateEstoque;
    }

    @Exclude
    public void setStateEstoque(StateEstoque stateEstoque) {
        this.stateEstoque = stateEstoque;
    }
}
