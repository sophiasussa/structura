package com.example.application.controller;

import java.util.List;

import com.example.application.model.Fornecedor;
import com.example.application.repository.DaoFornecedor;

public class ControllerFornecedor {
    DaoFornecedor dao = new DaoFornecedor();

    public boolean inserir(Fornecedor fornecedor){
        return dao.inserir(fornecedor);
    }

    public boolean alterar(Fornecedor fornecedor){
        return dao.alterar(fornecedor);
    }

    public boolean excluir(Fornecedor fornecedor){
        return dao.excluir(fornecedor);
    }

    public List<Fornecedor> pesquisarTodos(){
        return dao.pesquisarTodos();
    }

    public List<Fornecedor> pesquisaFornecedors(String pesquisa){
        return dao.pesquisarFornecedor(pesquisa);
    }

}
