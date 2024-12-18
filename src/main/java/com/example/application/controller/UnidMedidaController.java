package com.example.application.controller;

import java.util.List;

import com.example.application.model.UnidMedida;
import com.example.application.repository.UnidMedidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnidMedidaController {

    private static final Logger logger = LoggerFactory.getLogger(UnidMedidaController.class);
    private UnidMedidaRepository daoUnidMedida;

    public UnidMedidaController() {
        try {
            this.daoUnidMedida = new UnidMedidaRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(UnidMedida unidMedida) {
        if (unidMedida == null) {
            logger.warn("Tentativa de inserir unidade de medida com valor nulo");
            return false;
        }
        try {
            return daoUnidMedida.inserir(unidMedida);
        } catch (Exception e) {
            logger.error("Erro ao inserir unidade de medida", e);
            return false;
        }
    }

    public boolean alterar(UnidMedida unidMedida) {
        if (unidMedida == null) {
            logger.warn("Tentativa de alterar unidade de medida com valor nulo");
            return false;
        }
        try {
            return daoUnidMedida.alterar(unidMedida);
        } catch (Exception e) {
            logger.error("Erro ao alterar unidade de medida", e);
            return false;
        }
    }

    public boolean excluir(UnidMedida unidMedida) {
        if (unidMedida == null) {
            logger.warn("Tentativa de excluir unidade de medida com valor nulo");
            return false;
        }
        try {
            return daoUnidMedida.excluir(unidMedida);
        } catch (Exception e) {
            logger.error("Erro ao excluir unidade de medida", e);
            return false;
        }
    }

    public List<UnidMedida> pesquisarTodos() {
        try {
            return daoUnidMedida.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as unidades de medida", e);
            return null;
        }
    }
}