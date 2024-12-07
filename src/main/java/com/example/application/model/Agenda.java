package com.example.application.model;

import java.time.LocalDate;

public class Agenda {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataHora;
    private StatusAgenda status;
    private Funcionario funcionario;

    public Agenda() {
    }
    
    public Agenda(Long id, String titulo, String descricao, LocalDate dataHora, StatusAgenda status,
            Funcionario funcionario) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.status = status;
        this.funcionario = funcionario;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDate getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDate dataHora) {
        this.dataHora = dataHora;
    }
    public StatusAgenda getStatus() {
        return status;
    }
    public void setStatus(StatusAgenda status) {
        this.status = status;
    }
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

}
