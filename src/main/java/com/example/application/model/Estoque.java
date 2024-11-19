package com.example.application.model;

import java.math.BigDecimal;

public class Estoque {
    private int quantidadeAtual;
    private int quantidadeMinima;
    private BigDecimal custoUnitario;

    public Estoque(int quantidadeAtual, int quantidadeMinima, BigDecimal custoUnitario) {
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.custoUnitario = custoUnitario;
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

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }
}
