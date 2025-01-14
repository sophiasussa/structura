package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.model.Cliente;
import com.example.application.model.Produto;
import com.example.application.repository.ProdutoRepository;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private ProdutoRepository daoProduto;

    public ProdutoController() {
        try {
            this.daoProduto = new ProdutoRepository();
        } catch (SQLException e) {
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
            boolean sucesso = daoProduto.inserir(produto);
            if (sucesso) {
                logger.info("Produto inserido com sucesso: " + produto.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o produto: " + produto.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir produto: " + produto.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de alterar produto com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoProduto.alterar(produto);
            if (sucesso) {
                logger.info("Produto atualizado com sucesso: " + produto.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o produto com ID: " + produto.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar produto: " + produto.getId(), e);
            return false;
        }
    }

    public boolean excluir(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de excluir produto com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoProduto.excluir(produto);
            if (sucesso) {
                logger.info("Produto excluído com sucesso: " + produto.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o produto com ID: " + produto.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir produto: " + produto.getId(), e);
            return false;
        }
    }

    public List<Produto> pesquisarTodos() {
        try {
            List<Produto> lista = daoProduto.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " produtos.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os produtos", e);
            return new ArrayList<>();
        }
    }

    public List<Produto> pesquisarProduto(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de produto com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Produto> lista = daoProduto.pesquisarProduto(pesquisa);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " produtos para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar produto com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }

    public Produto getProdutoById(int id) {
        try {
            Produto produto = daoProduto.getProdutoById(id);
            if (produto != null) {
                logger.info("Produto encontrado pelo ID: " + id);
            } else {
                logger.warn("Nenhum produto encontrado com o ID: " + id);
            }
            return produto;
        } catch (Exception e) {
            logger.error("Erro ao buscar produto com ID: " + id, e);
            return null;
        }
    }

    public List<Produto> pesquisarProdutoComQuantidadeMinimaIgual() {
        try {
            List<Produto> lista = daoProduto.pesquisarProdutoComQuantidadeMinimaIgual();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " produtos com quantidade mínima igual.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com quantidade mínima igual", e);
            return new ArrayList<>();
        }
    }
}