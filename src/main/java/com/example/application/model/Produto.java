package com.example.application.model;

import java.math.BigDecimal;

public class Produto extends Estoque{
    private Long id;
    private Material material;
    private UnidMedida unidMedida;
    private Modelo modelo;

    public Produto(String nome, Integer quantidadeAtual, Integer quantidadeMinima, Double custoUnitario, Material material, UnidMedida unidMedida, Modelo modelo) {
        super(nome, quantidadeAtual, quantidadeMinima, custoUnitario);
        this.material = material;
        this.unidMedida = unidMedida;
        this.modelo = modelo;
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

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
}