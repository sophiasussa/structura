package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.example.application.model.Modelo;
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
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    private boolean validarUnidMedida(UnidMedida unidMedida) {
        if (unidMedida == null) {
            logger.warn("Tentativa de inserir ou alterar unidMedida com valor nulo");
            return false;
        }
        if (unidMedida.getNome() == null || unidMedida.getNome().isEmpty()) {
            logger.warn("Nome do unidMedida é obrigatório");
            return false;
        }
        if (unidMedida.getNome().length() > 100) {
            logger.warn("Nome do unidMedida não pode ter mais de 100 caracteres");
            return false;
        }
        return true;
    }

    public boolean inserir(UnidMedida unidMedida) {
        if (!validarUnidMedida(unidMedida)) {
            return false;
        }
        try {
            boolean sucesso = daoUnidMedida.inserir(unidMedida);
            if (sucesso) {
                logger.info("Unidade de medida inserida com sucesso: " + unidMedida.getNome());
            } else {
                logger.warn("Nenhuma linha inserida para a unidade de medida: " + unidMedida.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir unidade de medida", e);
            return false;
        }
    }

    public boolean alterar(UnidMedida unidMedida) {
        if (!validarUnidMedida(unidMedida)) {
            return false;
        }
        try {
            boolean sucesso = daoUnidMedida.alterar(unidMedida);
            if (sucesso) {
                logger.info("Unidade de medida alterada com sucesso: " + unidMedida.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para a unidade de medida: " + unidMedida.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar unidade de medida", e);
            return false;
        }
    }

    public boolean isUnidMedidaInUse(UnidMedida unidMedida) {
        try {
            return daoUnidMedida.isUnidMedidaInUse(unidMedida);
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as unidades de medida", e);
            return false;
        }
    }

    public boolean excluir(UnidMedida unidMedida) {
        if (unidMedida == null) {
            logger.warn("Tentativa de excluir unidade de medida com valor nulo");
            return false;
        }else if(isUnidMedidaInUse(unidMedida)){
            logger.warn("Não é possível excluir o unidMedida. O unidMedida está associado a um produto");
        }
        try {
            boolean sucesso = daoUnidMedida.excluir(unidMedida);
            if (sucesso) {
                logger.info("Unidade de medida excluída com sucesso: " + unidMedida.getId());
            } else {
                logger.warn("Nenhuma linha excluída para a unidade de medida com ID: " + unidMedida.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir unidade de medida", e);
            return false;
        }
    }

    public List<UnidMedida> pesquisarTodos() {
        try {
            List<UnidMedida> lista = daoUnidMedida.pesquisarTodos();
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " unidades de medida.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as unidades de medida", e);
            return new ArrayList<>();
        }
    }
}
