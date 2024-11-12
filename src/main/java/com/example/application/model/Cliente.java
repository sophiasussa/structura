package com.example.application.model;

public class Cliente extends Pessoa {
    private Long id;

    public Cliente(String nome, String cpf, String rg, String telefone) {
        super(nome, cpf, rg, telefone);
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
