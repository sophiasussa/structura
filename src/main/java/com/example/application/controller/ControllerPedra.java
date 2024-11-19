package com.example.application.controller;

import java.util.List;

import com.example.application.model.Funcionario;
import com.example.application.model.Pedra;
import com.example.application.repository.DaoPedra;

public class ControllerPedra {
    DaoPedra dao = new DaoPedra();

    public boolean inserir(Pedra pedra){
        return dao.inserir(pedra);
    }

    public boolean alterar(Pedra pedra){
        return dao.alterar(pedra);
    }

    public boolean excluir(Pedra pedra){
        return dao.excluir(pedra);
    }

    public List<Pedra> pesquisarTodos(){
        return dao.pesquisarTodos();
    }

    public List<Pedra> pesquisarPedra(String pesquisa){
        return dao.pesquisarPedra(pesquisa);
    }
}
