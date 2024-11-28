package com.example.application.model;

import java.math.BigDecimal;

public class Produto extends Estoque{
    private Long id;
    private String modelo;
    private Material material;
    private UnidMedida unidMedida;

    public Produto(String nome, String modelo, int quantidadeAtual, int quantidadeMinima, BigDecimal custoUnitario, Material material, UnidMedida unidMedida) {
        super(nome, quantidadeAtual, quantidadeMinima, custoUnitario);
        this.modelo = modelo;
        this.material = material;
        this.unidMedida = unidMedida;
    }

    public Produto(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public UnidMedida getUnidMedida() {
        return unidMedida;
    }

    public void setUnidMedida(UnidMedida unidMedida) {
        this.unidMedida = unidMedida;
    }
}
