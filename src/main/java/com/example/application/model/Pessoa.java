package com.example.application.model;

public class Pessoa {
    private String nome;
    private long cpf;
    private long rg;
    private Long telefone;
    private String endereco;

    public Pessoa(String nome, long cpf, long rg, Long telefone, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Pessoa() {

    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCpf() {
        return cpf;
    }
    public void setCpf(long cpf) {
        this.cpf = cpf;
    }
    public long getRg() {
        return rg;
    }
    public void setRg(long rg) {
        this.rg = rg;
    }
    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

}