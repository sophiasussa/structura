package com.example.application.model;

public class Fornecedor extends Pessoa{
    private Long id;
    private String descriProdu;

    public Fornecedor(String nome, Long cpf, Long rg, long telefone,
            long id, String descriProdu) {
        super(nome, cpf, rg, telefone);
        this.id = id;
        this.descriProdu = descriProdu;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getDescriProdu(){
        return descriProdu;
    }

    public void setDescriProdu(String descriProdu){
        this.descriProdu = descriProdu;
    }
}
