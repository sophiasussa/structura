package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

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
        } catch (SQLException e) {
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
            boolean sucesso = daoMaterial.inserir(material);
            if (sucesso) {
                logger.info("Material inserido com sucesso: " + material.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o material: " + material.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir material: " + material.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Material material) {
        if (material == null) {
            logger.warn("Tentativa de alterar material com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoMaterial.alterar(material);
            if (sucesso) {
                logger.info("Material atualizado com sucesso: " + material.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o material com ID: " + material.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar material: " + material.getNome(), e);
            return false;
        }
    }

    public boolean excluir(Material material) {
        if (material == null) {
            logger.warn("Tentativa de excluir material com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoMaterial.excluir(material);
            if (sucesso) {
                logger.info("Material excluído com sucesso: " + material.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o material com ID: " + material.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir material com ID: " + material.getId(), e);
            return false;
        }
    }

    public List<Material> pesquisarTodos() {
        try {
            List<Material> lista = daoMaterial.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " materiais.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os materiais", e);
            return new ArrayList<>();
        }
    }
}


