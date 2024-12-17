package com.example.application.model;

public class Fornecedor {
    private Long id;
    private String empresa;
    private String vendedor;
    private String cnpj;
    private String ie;
    private String telefone;
    private String email;
    private String descriProdutos;

    public Fornecedor(String empresa, String vendedor, String cnpj, String ie, String telefone, String email, String descriProdutos) {
        this.empresa = empresa;
        this.vendedor = vendedor;
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

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
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
