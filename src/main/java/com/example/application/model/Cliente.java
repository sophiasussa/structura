package com.example.application.model;

public class Cliente extends Pessoa {
    private Long id;

    public Cliente(String nome, int cpf, int rg, long telefone, String endereco,
        long id, String descriProdu) {
    super(nome, cpf, rg, telefone, endereco);
    this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
}
