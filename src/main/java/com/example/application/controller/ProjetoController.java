package com.example.application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.Projeto;
import com.example.application.repository.ProjetoRepository;

public class ProjetoController {

    private static final Logger logger = LoggerFactory.getLogger(ProjetoController.class);
    private ProjetoRepository daoProjeto;

    public ProjetoController() {
        try {
            this.daoProjeto = new ProjetoRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    private boolean validarProjeto(Projeto projeto) {
        if (projeto == null) {
            logger.warn("Tentativa de inserir projeto com valor nulo");
            return false;
        }
        if (projeto.getDescricao() == null || projeto.getDescricao().isEmpty()) {
            logger.warn("Descrição do projeto é obrigatória");
            return false;
        }
        if (projeto.getValor() < 0) {
            logger.warn("Valor do projeto não pode ser negativo");
            return false;
        }
        if (projeto.getCliente() == null || projeto.getCliente().getId() == null) {
            logger.warn("Projeto deve estar associado a um cliente válido");
            return false;
        }
        return true;
    }

    public boolean inserir(Projeto projeto) {
        if (!validarProjeto(projeto)) {
            return false;
        }
        try {
            boolean sucesso = daoProjeto.inserir(projeto);
            if (sucesso) {
                logger.info("Projeto inserido com sucesso: " + projeto.getDescricao());
            } else {
                logger.warn("Nenhuma linha foi inserida para o projeto: " + projeto.getDescricao());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir projeto: " + projeto.getDescricao(), e);
            return false;
        }
    }

    public boolean alterar(Projeto projeto) {
        if (!validarProjeto(projeto)) {
            return false;
        }
        try {
            boolean sucesso = daoProjeto.alterar(projeto);
            if (sucesso) {
                logger.info("Projeto atualizado com sucesso: " + projeto.getDescricao());
            } else {
                logger.warn("Nenhuma linha atualizada para o projeto com ID: " + projeto.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar projeto", e);
            return false;
        }
    }

    public boolean excluir(Projeto projeto) {
        if (projeto == null || projeto.getId() == null) {
            logger.warn("Tentativa de excluir projeto com valor nulo ou ID inválido");
            return false;
        }
        try {
            boolean sucesso = daoProjeto.excluir(projeto);
            if (sucesso) {
                logger.info("Projeto excluído com sucesso: " + projeto.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o projeto com ID: " + projeto.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir projeto", e);
            return false;
        }
    }

    public List<Projeto> pesquisarTodos() {
        try {
            List<Projeto> lista = daoProjeto.pesquisarTodos();
            if (lista == null) {
                lista = new ArrayList<>();
            }
            logger.info("Pesquisados " + lista.size() + " projetos.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os projetos", e);
            return new ArrayList<>();
        }
    }

    public List<Projeto> pesquisarProjeto(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de projeto com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }

        try {
            List<Projeto> lista = daoProjeto.pesquisarProjeto(pesquisa);
            if (lista == null) {
                lista = new ArrayList<>();
            }
            logger.info("Pesquisados " + lista.size() + " projetos para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar projeto com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }

    public Projeto getProjetoById(long id) {
        if (id <= 0) {
            logger.warn("ID de projeto inválido: " + id);
            return null;
        }

        try {
            Projeto projeto = daoProjeto.getProjetoById(id);
            if (projeto != null) {
                logger.info("Projeto encontrado pelo ID: " + id);
            } else {
                logger.warn("Nenhum projeto encontrado com o ID: " + id);
            }
            return projeto;
        } catch (Exception e) {
            logger.error("Erro ao buscar projeto com ID: " + id, e);
            return null;
        }
    }
}
