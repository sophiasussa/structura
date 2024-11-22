package com.example.application.model;

import java.math.BigDecimal;

public class Pedra extends Estoque{
    private Long id;
    private String modelo;
    private Cor cor;
    private Material material;
    private UnidMedida unidMedida;

    public Pedra(String nome, String modelo, int quantidadeAtual, int quantidadeMinima, BigDecimal custoUnitario, Cor cor, Material material, UnidMedida unidMedida) {
        super(nome, quantidadeAtual, quantidadeMinima, custoUnitario);
        this.modelo = modelo;
        this.cor = cor;
        this.material = material;
        this.unidMedida = unidMedida;
    }

    public Pedra(){
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

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
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
