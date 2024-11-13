package com.example.application.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario extends Pessoa  {
    private Long id;
    private LocalDate dataAdmissao;
    private BigDecimal salario;

    public Funcionario(String nome, String cpf, String rg, String telefone, LocalDate dataAdmissao, BigDecimal salario) {
        super(nome, cpf, rg, telefone);
        this.dataAdmissao = dataAdmissao;
        this.salario = salario;
    }

    public Funcionario(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
 
    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public BigDecimal getSalario(){
        return salario;
    }

    public void setSalario(BigDecimal salario){
        this.salario = salario;
    }

}
