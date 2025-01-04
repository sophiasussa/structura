package com.example.application.model;

public enum EntregaOS {
    NAO_DEFINIDA("Não Definida"),
    NAO_ENTREGUE("Não Entregue"),
    PRIMEIRA_ENTREGA("Primeira Entrega"),
    SEGUNDA_ENTREGA("Segunda Entrega"),
    TERCEIRA_ENTREGA("Terceira Entrega"),
    ENTREGA_FINAL("Entrega Finalizada");

    private final String descricao;

    EntregaOS(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

