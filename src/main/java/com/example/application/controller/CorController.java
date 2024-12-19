package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.example.application.model.Cor;
import com.example.application.repository.CorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorController {

    private static final Logger logger = LoggerFactory.getLogger(CorController.class);
    private CorRepository daoCor;

    public CorController() {
        try {
            this.daoCor = new CorRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Cor cor) {
        if (cor == null) {
            logger.warn("Tentativa de inserir cor com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoCor.inserir(cor);
            if (sucesso) {
                logger.info("Cor inserida com sucesso: " + cor.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para a cor: " + cor.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir cor: " + cor.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Cor cor) {
        if (cor == null) {
            logger.warn("Tentativa de alterar cor com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoCor.alterar(cor);
            if (sucesso) {
                logger.info("Cor atualizada com sucesso: " + cor.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para a cor com ID: " + cor.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar cor: " + cor.getNome(), e);
            return false;
        }
    }

    public boolean excluir(Cor cor) {
        if (cor == null) {
            logger.warn("Tentativa de excluir cor com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoCor.excluir(cor);
            if (sucesso) {
                logger.info("Cor excluída com sucesso: " + cor.getId());
            } else {
                logger.warn("Nenhuma linha excluída para a cor com ID: " + cor.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir cor com ID: " + cor.getId(), e);
            return false;
        }
    }

    public List<Cor> pesquisarTodos() {
        try {
            List<Cor> lista = daoCor.pesquisarTodos();
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " cores.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as cores", e);
            return new ArrayList<>();
        }
    }
}