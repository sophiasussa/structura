package com.example.application.model;

public class ImagemOS {
    private Long id;
    private OrdemServico ordemServico;
    private String caminhoImagem;

    public ImagemOS() {}

    public ImagemOS(OrdemServico ordemServico, String caminhoImagem) {
        this.ordemServico = ordemServico;
        this.caminhoImagem = caminhoImagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
}