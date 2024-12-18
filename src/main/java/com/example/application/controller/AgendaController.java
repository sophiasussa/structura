package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.repository.AgendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgendaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);
    private AgendaRepository daoAgenda;

    public AgendaController() {
        try {
            this.daoAgenda = new AgendaRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Agenda agenda) {
        if (agenda == null) {
            logger.warn("Tentativa de inserir agenda com valor nulo");
            return false;
        }
        try {
            return daoAgenda.inserir(agenda);
        } catch (Exception e) {
            logger.error("Erro ao inserir agenda", e);
            return false;
        }
    }

    public boolean alterar(Agenda agenda) {
        if (agenda == null) {
            logger.warn("Tentativa de alterar agenda com valor nulo");
            return false;
        }
        try {
            return daoAgenda.alterar(agenda);
        } catch (Exception e) {
            logger.error("Erro ao alterar agenda", e);
            return false;
        }
    }

    public boolean excluir(Agenda agenda) {
        if (agenda == null) {
            logger.warn("Tentativa de excluir agenda com valor nulo");
            return false;
        }
        try {
            return daoAgenda.excluir(agenda);
        } catch (Exception e) {
            logger.error("Erro ao excluir agenda", e);
            return false;
        }
    }

    public List<Agenda> pesquisarTodos() {
        try {
            return daoAgenda.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as agendas", e);
            return null;
        }
    }

    public List<Agenda> pesquisarAgenda(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de agenda com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoAgenda.pesquisarAgenda(pesquisa);
        } catch (Exception e) {
            logger.error("Erro ao buscar agenda com pesquisa: " + pesquisa, e);
            return null;
        }
    }

    public List<Agenda> pesquisarTarefasDeHoje() {
        try {
            return daoAgenda.pesquisarTarefasDeHoje();
        } catch (Exception e) {
            logger.error("Erro ao buscar tarefas de hoje", e);
            return null;
        }
    }
}

