package com.example.application.model;

public class Fornecedor {
    private Long id;
    private String nome;
    private String nomeVendedor;
    private String cnpj;
    private String ie;
    private String telefone;
    private String email;
    private String descriProdutos;

    // Construtor com parâmetros
    public Fornecedor(String nome, String nomeVendedor, String cnpj, String ie, String telefone, String email, String descriProdutos) {
        this.nome = nome;
        this.nomeVendedor = nomeVendedor;
        this.cnpj = cnpj;
        this.ie = ie;
        this.telefone = telefone;
        this.email = email;
        this.descriProdutos = descriProdutos;
    }

    public Fornecedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescriProdutos() {
        return descriProdutos;
    }

    public void setDescriProdutos(String descriProdutos) {
        this.descriProdutos = descriProdutos;
    }
}
