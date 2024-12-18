package com.example.application.controller;

import java.util.List;

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
        } catch (Exception e) {
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
            return daoCor.inserir(cor);
        } catch (Exception e) {
            logger.error("Erro ao inserir cor", e);
            return false;
        }
    }

    public boolean alterar(Cor cor) {
        if (cor == null) {
            logger.warn("Tentativa de alterar cor com valor nulo");
            return false;
        }
        try {
            return daoCor.alterar(cor);
        } catch (Exception e) {
            logger.error("Erro ao alterar cor", e);
            return false;
        }
    }

    public boolean excluir(Cor cor) {
        if (cor == null) {
            logger.warn("Tentativa de excluir cor com valor nulo");
            return false;
        }
        try {
            return daoCor.excluir(cor);
        } catch (Exception e) {
            logger.error("Erro ao excluir cor", e);
            return false;
        }
    }

    public List<Cor> pesquisarTodos() {
        try {
            return daoCor.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as cores", e);
            return null;
        }
    }
}
