package com.example.application.controller;

import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.repository.DaoFuncionario;

public class ControllerFuncionario {
    DaoFuncionario dao = new DaoFuncionario();

    public boolean inserir(Funcionario funcionario){
        return dao.inserir(funcionario);
    }

    public boolean alterar(Funcionario funcionario){
        return dao.alterar(funcionario);
    }

    public boolean excluir(Funcionario funcionario){
        return dao.excluir(funcionario);
    }

    public List<Funcionario> pesquisarTodos(){
        return dao.pesquisarTodos();
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa){
        return dao.pesquisarFuncionario(pesquisa);
    }

    public Funcionario getFuncionarioById(int id) {
        return dao.getFuncionarioById(id);
    }
}
