package com.example.application.controller;

import java.util.List;

import com.example.application.model.UnidMedida;
import com.example.application.repository.DaoUnidMedida;

public class ControllerUnidMedida {
    DaoUnidMedida dao = new DaoUnidMedida();

    public boolean inserir(UnidMedida unidMedida){
        return dao.inserir(unidMedida);
    }

    public boolean alterar(UnidMedida unidMedida){
        return dao.alterar(unidMedida);
    }

    public boolean excluir(UnidMedida unidMedida){
        return dao.excluir(unidMedida);
    }

    public List<UnidMedida> pesquisarTodos(){
        return dao.pesquisarTodos();
    }
}
