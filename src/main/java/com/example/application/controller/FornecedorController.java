package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Fornecedor;
import com.example.application.repository.FornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class FornecedorController {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);
    private FornecedorRepository daoFornecedor;

    public FornecedorController() {
        try {
            this.daoFornecedor = new FornecedorRepository();
        } catch (SQLException e) {
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
            boolean sucesso = daoFornecedor.inserir(fornecedor);
            if (sucesso) {
                logger.info("Fornecedor inserido com sucesso: " + fornecedor.getVendedor());
            } else {
                logger.warn("Nenhuma linha foi inserida para o fornecedor: " + fornecedor.getVendedor());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir fornecedor: " + fornecedor.getVendedor(), e);
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            logger.warn("Tentativa de alterar fornecedor com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoFornecedor.alterar(fornecedor);
            if (sucesso) {
                logger.info("Fornecedor atualizado com sucesso: " + fornecedor.getVendedor());
            } else {
                logger.warn("Nenhuma linha atualizada para o fornecedor com ID: " + fornecedor.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar fornecedor: " + fornecedor.getVendedor(), e);
            return false;
        }
    }
    
    public boolean excluir(Fornecedor fornecedor) {
        if (fornecedor == null) {
            logger.warn("Tentativa de excluir fornecedor com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoFornecedor.excluir(fornecedor);
            if (sucesso) {
                logger.info("Fornecedor excluído com sucesso: " + fornecedor.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o fornecedor com ID: " + fornecedor.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir fornecedor com ID: " + fornecedor.getId(), e);
            return false;
        }
    }

    public List<Fornecedor> pesquisarTodos() {
        try {
            List<Fornecedor> lista = daoFornecedor.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " fornecedores.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os fornecedores", e);
            return new ArrayList<>();
        }
    }

    public List<Fornecedor> pesquisarFornecedor(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de fornecedor com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Fornecedor> lista = daoFornecedor.pesquisarFornecedor(pesquisa);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " fornecedores para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar fornecedor com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }
}