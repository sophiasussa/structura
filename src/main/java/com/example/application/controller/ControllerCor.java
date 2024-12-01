package com.example.application.controller;

import java.util.List;

import com.example.application.model.Cor;
import com.example.application.repository.DaoCor;

public class ControllerCor {
    DaoCor dao = new DaoCor();

    public boolean inserir(Cor cor){
        return dao.inserir(cor);
    }

    public boolean alterar(Cor cor){
        return dao.alterar(cor);
    }

    public boolean excluir(Cor cor){
        return dao.excluir(cor);
    }

    public List<Cor> pesquisarTodos(){
        return dao.pesquisarTodos();
    }
}
