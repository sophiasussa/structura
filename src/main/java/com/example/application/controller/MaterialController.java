package com.example.application.controller;

import java.util.List;

import com.example.application.model.Material;
import com.example.application.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private MaterialRepository daoMaterial;

    public MaterialController() {
        try {
            this.daoMaterial = new MaterialRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Material material) {
        if (material == null) {
            logger.warn("Tentativa de inserir material com valor nulo");
            return false;
        }
        try {
            return daoMaterial.inserir(material);
        } catch (Exception e) {
            logger.error("Erro ao inserir material", e);
            return false;
        }
    }

    public boolean alterar(Material material) {
        if (material == null) {
            logger.warn("Tentativa de alterar material com valor nulo");
            return false;
        }
        try {
            return daoMaterial.alterar(material);
        } catch (Exception e) {
            logger.error("Erro ao alterar material", e);
            return false;
        }
    }

    public boolean excluir(Material material) {
        if (material == null) {
            logger.warn("Tentativa de excluir material com valor nulo");
            return false;
        }
        try {
            return daoMaterial.excluir(material);
        } catch (Exception e) {
            logger.error("Erro ao excluir material", e);
            return false;
        }
    }

    public List<Material> pesquisarTodos() {
        try {
            return daoMaterial.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os materiais", e);
            return null;
        }
    }
}


