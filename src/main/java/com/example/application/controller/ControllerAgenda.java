package com.example.application.controller;

import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.repository.DaoAgenda;

public class ControllerAgenda {
    DaoAgenda dao = new DaoAgenda();

    public boolean inserir(Agenda agenda){
        return dao.inserir(agenda);
    }

    public boolean alterar(Agenda agenda){
        return dao.alterar(agenda);
    }

    public boolean excluir(Agenda agenda){
        return dao.excluir(agenda);
    }

    public List<Agenda> pesquisarTodos(){
        return dao.pesquisarTodos();
    }

    public List<Agenda> pesquisarAgenda(String pesquisa){
        return dao.pesquisarAgenda(pesquisa);
    }
}
