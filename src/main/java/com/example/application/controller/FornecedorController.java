package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Fornecedor;
import com.example.application.repository.FornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FornecedorController {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);
    private FornecedorRepository daoFornecedor;

    public FornecedorController() {
        try {
            this.daoFornecedor = new FornecedorRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Fornecedor fornecedor) {
        if (fornecedor == null) {
            logger.warn("Tentativa de inserir fornecedor com valor nulo");
            return false;
        }
        try {
            return daoFornecedor.inserir(fornecedor);
        } catch (Exception e) {
            logger.error("Erro ao inserir fornecedor", e);
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            logger.warn("Tentativa de alterar fornecedor com valor nulo");
            return false;
        }
        try {
            return daoFornecedor.alterar(fornecedor);
        } catch (Exception e) {
            logger.error("Erro ao alterar fornecedor", e);
            return false;
        }
    }

    public boolean excluir(Fornecedor fornecedor) {
        if (fornecedor == null) {
            logger.warn("Tentativa de excluir fornecedor com valor nulo");
            return false;
        }
        try {
            return daoFornecedor.excluir(fornecedor);
        } catch (Exception e) {
            logger.error("Erro ao excluir fornecedor", e);
            return false;
        }
    }

    public List<Fornecedor> pesquisarTodos() {
        try {
            return daoFornecedor.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os fornecedores", e);
            return null;
        }
    }

    public List<Fornecedor> pesquisaFornecedors(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de fornecedor com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoFornecedor.pesquisarFornecedor(pesquisa);
        } catch (Exception e) {
            logger.error("Erro ao buscar fornecedor com pesquisa: " + pesquisa, e);
            return null;
        }
    }
}
