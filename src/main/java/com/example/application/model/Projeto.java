package com.example.application.model;
import java.time.LocalDate;

public class Projeto {
    private Long id;
    private StatusProjeto statusPr;
    private LocalDate data;
    private Double valor;
    private Double desconto;
    private Double valorFinal;
    private String descricao;
    private String medidas;
    private Cliente cliente;

    public Projeto() {}

    public Projeto(StatusProjeto statusPr, LocalDate data, Double valor, Double desconto,
                   Double valorFinal, String descricao, String medidas, Cliente cliente) {
        this.statusPr = statusPr;
        this.data = data;
        this.valor = valor;
        this.desconto = desconto;
        this.valorFinal = valorFinal;
        this.descricao = descricao;
        this.medidas = medidas;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusProjeto getStatusPr() {
        return statusPr;
    }

    public void setStatusPr(StatusProjeto statusPr) {
        this.statusPr = statusPr;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}