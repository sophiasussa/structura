package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Produto;
import com.example.application.repository.DaoProduto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerProduto {

    private static final Logger logger = LoggerFactory.getLogger(ControllerProduto.class);
    private DaoProduto daoProduto;

    public ControllerProduto() {
        try {
            this.daoProduto = new DaoProduto();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de inserir produto com valor nulo");
            return false;
        }
        try {
            return daoProduto.inserir(produto);
        } catch (Exception e) {
            logger.error("Erro ao inserir produto", e);
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de alterar produto com valor nulo");
            return false;
        }
        try {
            return daoProduto.alterar(produto);
        } catch (Exception e) {
            logger.error("Erro ao alterar produto", e);
            return false;
        }
    }

    public boolean excluir(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de excluir produto com valor nulo");
            return false;
        }
        try {
            return daoProduto.excluir(produto);
        } catch (Exception e) {
            logger.error("Erro ao excluir produto", e);
            return false;
        }
    }

    public List<Produto> pesquisarTodos() {
        try {
            return daoProduto.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os produtos", e);
            return null;
        }
    }

    public List<Produto> pesquisarProduto(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de produto com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoProduto.pesquisarProduto(pesquisa);
        } catch (Exception e) {
            logger.error("Erro ao buscar produto com pesquisa: " + pesquisa, e);
            return null;
        }
    }

    public Produto getProdutoById(int id) {
        try {
            return daoProduto.getProdutoById(id);
        } catch (Exception e) {
            logger.error("Erro ao buscar produto com ID: " + id, e);
            return null;
        }
    }

    public List<Produto> pesquisarProdutoComQuantidadeMinimaIgual() {
        try {
            return daoProduto.pesquisarProdutoComQuantidadeMinimaIgual();
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com quantidade mínima igual", e);
            return null;
        }
    }
}