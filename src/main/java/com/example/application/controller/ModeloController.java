package com.example.application.controller;

import java.util.List;

import com.example.application.model.Modelo;
import com.example.application.repository.ModeloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModeloController {

    private static final Logger logger = LoggerFactory.getLogger(ModeloController.class);
    private ModeloRepository daoModelo;

    public ModeloController() {
        try {
            this.daoModelo = new ModeloRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Modelo modelo) {
        if (modelo == null) {
            logger.warn("Tentativa de inserir modelo com valor nulo");
            return false;
        }
        try {
            return daoModelo.inserir(modelo);
        } catch (Exception e) {
            logger.error("Erro ao inserir modelo", e);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        if (modelo == null) {
            logger.warn("Tentativa de alterar modelo com valor nulo");
            return false;
        }
        try {
            return daoModelo.alterar(modelo);
        } catch (Exception e) {
            logger.error("Erro ao alterar modelo", e);
            return false;
        }
    }

    public boolean excluir(Modelo modelo) {
        if (modelo == null) {
            logger.warn("Tentativa de excluir modelo com valor nulo");
            return false;
        }
        try {
            return daoModelo.excluir(modelo);
        } catch (Exception e) {
            logger.error("Erro ao excluir modelo", e);
            return false;
        }
    }

    public List<Modelo> pesquisarTodos() {
        try {
            return daoModelo.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os modelos", e);
            return null;
        }
    }
}

