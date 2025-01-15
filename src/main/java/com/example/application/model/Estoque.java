package com.example.application.model;

public abstract class Estoque {
    private String nome;
    private Integer quantidadeAtual;
    private Integer quantidadeMinima;
    private Double custoUnitario;

    protected Estoque(String nome, Integer quantidadeAtual, Integer quantidadeMinima, Double custoUnitario) {
        this.nome = nome;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.custoUnitario = custoUnitario;
    }

    protected Estoque(){
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public Double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(Double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }
}