package com.example.application.model;

import java.time.LocalDate;
import java.util.List;

public class OrdemServico {
    private Long id;
    private StatusOS statusOS;
    private Cliente cliente;
    private List<String> imagens;
    private String endereco;
    private Funcionario funcionario;
    private LocalDate dataAbertura;
    private LocalDate dataPrevFinaliza;
    private String observacao;
    private String imagem;

    public OrdemServico() {
    }

    public OrdemServico(StatusOS statusOS, Cliente cliente, String endereco,
            Funcionario funcionario, LocalDate dataAbertura, LocalDate dataPrevFinaliza, String observacao) {
        this.statusOS = statusOS;
        this.cliente = cliente;
        this.endereco = endereco;
        this.funcionario = funcionario;
        this.dataAbertura = dataAbertura;
        this.dataPrevFinaliza = dataPrevFinaliza;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusOS getStatusOS() {
        return statusOS;
    }

    public void setStatusOS(StatusOS statusOS) {
        this.statusOS = statusOS;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataPrevFinaliza() {
        return dataPrevFinaliza;
    }

    public void setDataPrevFinaliza(LocalDate dataPrevFinaliza) {
        this.dataPrevFinaliza = dataPrevFinaliza;
    }
}
