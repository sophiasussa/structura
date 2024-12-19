package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

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
        } catch (SQLException e) {
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
            boolean sucesso = daoAgenda.inserir(agenda);
            if (sucesso) {
                logger.info("Agenda inserida com sucesso: " + agenda.getTitulo());
            } else {
                logger.warn("Nenhuma linha foi inserida para a agenda: " + agenda.getTitulo());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir agenda: " + agenda.getTitulo(), e);
            return false;
        }
    }

    public boolean alterar(Agenda agenda) {
        if (agenda == null) {
            logger.warn("Tentativa de alterar agenda com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoAgenda.alterar(agenda);
            if (sucesso) {
                logger.info("Agenda atualizada com sucesso: " + agenda.getTitulo());
            } else {
                logger.warn("Nenhuma linha foi atualizada para a agenda com ID: " + agenda.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar agenda: " + agenda.getTitulo(), e);
            return false;
        }
    }

    public boolean excluir(Agenda agenda) {
        if (agenda == null) {
            logger.warn("Tentativa de excluir agenda com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoAgenda.excluir(agenda);
            if (sucesso) {
                logger.info("Agenda excluída com sucesso: " + agenda.getId());
            } else {
                logger.warn("Nenhuma linha foi excluída para a agenda com ID: " + agenda.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir agenda com ID: " + agenda.getId(), e);
            return false;
        }
    }

    public List<Agenda> pesquisarTodos() {
        try {
            List<Agenda> lista = daoAgenda.pesquisarTodos();
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " agendas.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as agendas", e);
            return new ArrayList<>();
        }
    }

    public List<Agenda> pesquisarAgenda(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de agenda com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Agenda> lista = daoAgenda.pesquisarAgenda(pesquisa);
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " agendas para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar agendas com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }

    public List<Agenda> pesquisarTarefasDeHoje() {
        try {
            List<Agenda> lista = daoAgenda.pesquisarTarefasDeHoje();
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " tarefas de hoje.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar tarefas de hoje", e);
            return new ArrayList<>();
        }
    }
}