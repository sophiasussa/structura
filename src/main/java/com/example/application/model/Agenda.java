package com.example.application.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

public class Agenda {
    private Long id;
    private String titulo;
    private String descricao;
    private String endereco;
    private LocalDateTime dataHora;
    private StatusAgenda status;
    private Funcionario funcionario;

    public Agenda() {
    }
    
    public Agenda(StatusAgenda status, String titulo, String descricao, String endereco, LocalDateTime dataHora,
            Funcionario funcionario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.endereco = endereco;
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
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
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