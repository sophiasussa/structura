package com.example.application.model;
import java.time.LocalDate;

public class Funcionario extends Pessoa  {
    private Long id;
    private LocalDate dataAdmissao;
    private Double salario;

    public Funcionario(String nome, Long cpf, Long rg, long telefone,
            long id, LocalDate dataAdmissao, Double salario) {
        super(nome, cpf, rg, telefone);
        this.id = id;
        this.dataAdmissao = dataAdmissao;
        this.salario = salario;
    }

    public Long getId(){
        return id;
    }

    public void setIdFuncionario(Long id) {
        this.id = id;
    }
 
    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Double getSalario(){
        return salario;
    }

    public void setSalario(Double salario){
        this.salario = salario;
    }

}
