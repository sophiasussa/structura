package com.example.application.model;

public class Fornecedor extends Pessoa{
    private Long id;
    private String email;
    private String descriProdu;

    public Fornecedor(String nome, String cpf, String rg, String telefone, String email, String descriProdu) {
        super(nome, cpf, rg, telefone);
        this.email = email;
        this.descriProdu = descriProdu;
    }

    public Fornecedor(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescriProdu(){
        return descriProdu;
    }

    public void setDescriProdu(String descriProdu){
        this.descriProdu = descriProdu;
    }
}
