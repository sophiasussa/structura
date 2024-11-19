package com.example.application.controller;

import java.util.List;

import com.example.application.model.Material;
import com.example.application.repository.DaoMaterial;

public class ControllerMaterial {
    DaoMaterial dao = new DaoMaterial();

    public boolean inserir(Material material){
        return dao.inserir(material);
    }

    public boolean alterar(Material material){
        return dao.alterar(material);
    }

    public boolean excluir(Material material){
        return dao.excluir(material);
    }

    public List<Material> pesquisarTodos(){
        return dao.pesquisarTodos();
    }
}
