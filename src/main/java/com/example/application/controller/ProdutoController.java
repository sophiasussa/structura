package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.model.Cliente;
import com.example.application.model.Material;
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

    private boolean validarProduto(Produto produto) {
        if (produto == null) {
            logger.warn("Tentativa de inserir produto com valor nulo");
            return false;
        }
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            logger.warn("Nome do produto é obrigatório");
            return false;
        }
        if (produto.getNome().length() > 100) {
            logger.warn("Nome do produto não pode ter mais de 100 caracteres");
            return false;
        }
        if (produto.getQuantidadeAtual() < 0) {
            logger.warn("Quantidade atual do produto não pode ser negativa");
            return false;
        }
        if (produto.getQuantidadeMinima() < 0) {
            logger.warn("Quantidade mínima do produto não pode ser negativa");
            return false;
        }
        if (produto.getCustoUnitario() < 0) {
            logger.warn("Custo unitário do produto não pode ser negativo");
            return false;
        }
        return true;
    }

    public boolean inserir(Produto produto) {
        if (!validarProduto(produto)) {
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
        if (!validarProduto(produto)) {
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

    public boolean isProdutoInUse(Produto produto) {
        try {
            return daoProduto.isProdutoInUse(produto);
        } catch (Exception e) {
            logger.error("Erro ao verificar uso do Produto", e);
            return false;
        }
    }

    public String excluir(Produto produto) {
        if (produto == null || produto.getId() == null) {
            logger.warn("Tentativa de excluir produto com valor nulo ou ID inválido");
            return "Produto inválido ou não encontrado.";
        } else if (isProdutoInUse(produto)) {
            logger.warn("Não é possível excluir o produto. Ele está associado a um agendamento ou OS.");
            return "Não é possível excluir o produto, pois ele está associado a um agendamento ou OS.";
        }
        try {
            boolean sucesso = daoProduto.excluir(produto);
            if (sucesso) {
                logger.info("Produto excluído com sucesso: " + produto.getId());
                return null;
            } else {
                logger.warn("Nenhuma linha excluída para o produto com ID: " + produto.getId());
                return "Nenhuma exclusão realizada. Produto não encontrado.";
            }
        } catch (Exception e) {
            logger.error("Erro ao excluir produto com ID: " + produto.getId(), e);
            return "Erro interno ao tentar excluir o produto. Por favor, entre em contato com o suporte.";
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