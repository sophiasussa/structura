package com.example.application.model;

public enum StatusAgenda {
    ABERTA("Aberta"),
    EM_ANDAMENTO("Em_andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");


    private final String descricao;

    StatusAgenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}