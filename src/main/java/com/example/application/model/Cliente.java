package com.example.application.model;

public class Cliente extends Pessoa {
    private Long id;

    public Cliente(String nome, Long cpf, Long rg, long telefone, String endereco) {
        super(nome, cpf, rg, telefone, endereco);
    }

    public Cliente() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
