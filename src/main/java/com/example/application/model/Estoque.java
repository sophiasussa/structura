package com.example.application.model;

public class Estoque {
    private String nome;
    private int quantidadeAtual;
    private int quantidadeMinima;
    private Double custoUnitario;

    public Estoque(String nome, int quantidadeAtual, int quantidadeMinima, Double custoUnitario) {
        this.nome = nome;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.custoUnitario = custoUnitario;
    }

    public Estoque(){
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public Double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(Double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }
}
