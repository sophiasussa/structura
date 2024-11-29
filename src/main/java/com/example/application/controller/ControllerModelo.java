package com.example.application.controller;

import java.util.List;

import com.example.application.model.Modelo;
import com.example.application.repository.DaoModelo;

public class ControllerModelo {
   DaoModelo dao = new DaoModelo();

    public boolean inserir(Modelo modelo){
        return dao.inserir(modelo);
    }

    public boolean alterar(Modelo modelo){
        return dao.alterar(modelo);
    }

    public boolean excluir(Modelo modelo){
        return dao.excluir(modelo);
    }

    public List<Modelo> pesquisarTodos(){
        return dao.pesquisarTodos();
    }
}
