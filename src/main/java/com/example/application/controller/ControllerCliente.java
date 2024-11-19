package com.example.application.controller;

import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.repository.DaoCliente;

public class ControllerCliente {
    DaoCliente dao = new DaoCliente();

    public boolean inserir(Cliente cliente){
        return dao.inserir(cliente);
    }

    public boolean alterar(Cliente cliente){
        return dao.alterar(cliente);
    }

    public boolean excluir(Cliente cliente){
        return dao.excluir(cliente);
    }

    public List<Cliente> pesquisarTodos(){
        return dao.pesquisarTodos();
    }
    
    public List<Cliente> pesquisarCliente(String pesquisa){
        return dao.pesquisarCliente(pesquisa);
    }
}
