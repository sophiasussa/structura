package com.example.application.model;

public enum StatusProjeto {
    NOVO("Novo"),
    EM_ANALISE("Em análise"),
    APROVADO("Aprovado"),
    EM_EXECUCAO("Em execução"),
    PAUSADO("Pausado"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");

    private final String descricao;

    StatusProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
