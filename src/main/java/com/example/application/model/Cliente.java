package com.example.application.model;

public class Cliente {
    private Long id;
    private String nome;
    private Long cpf;
    private Long rg;
    private Long telefone;
    private String endereco;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public Long getCpf(){
        return cpf;
    }
    
    public void setCpf(Long cpf){
        this.cpf = cpf;
    }

    public Long getRg(){
        return rg;
    }

    public void setRg(Long rg){
        this.rg = rg;
    }

    public Long getTelefone(){
        return telefone;
    }

    public void setTelefone(Long telefone){
        this.telefone = telefone;
    }

    public String getEndereco(){
        return endereco;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }
}
