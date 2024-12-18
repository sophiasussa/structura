package com.example.application.controller;

import com.example.application.model.OrdemServico;
import com.example.application.repository.DaoOrdemServico;
import com.example.application.model.Produto;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerOS {

    private static final Logger logger = LoggerFactory.getLogger(ControllerOS.class);
    private final DaoOrdemServico daoOS;

    public ControllerOS() {
        try {
            this.daoOS = new DaoOrdemServico();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public Long saveOrdemServico(OrdemServico os) {
        if (os == null) {
            logger.warn("Tentativa de salvar Ordem de Serviço com valor nulo");
            return null;
        }
        try {
            return daoOS.saveOrdemServico(os);
        } catch (Exception e) {
            logger.error("Erro ao salvar Ordem de Serviço", e);
            return null;
        }
    }

    public List<OrdemServico> getAllOrdensServico() {
        try {
            return daoOS.getAllOrdensServico();
        } catch (Exception e) {
            logger.error("Erro ao buscar todas as Ordens de Serviço", e);
            return null;
        }
    }

    public boolean updateOrdemServico(OrdemServico os, List<Produto> produtos) {
        if (os == null) {
            logger.warn("Tentativa de atualizar Ordem de Serviço com valor nulo");
            return false;
        }
        try {
            return daoOS.updateOrdemServico(os, produtos);
        } catch (Exception e) {
            logger.error("Erro ao atualizar Ordem de Serviço", e);
            return false;
        }
    }

    public boolean deleteOrdemServico(OrdemServico os) {
        if (os == null) {
            logger.warn("Tentativa de excluir Ordem de Serviço com valor nulo");
            return false;
        }
        try {
            Long idOs = os.getId();
            return daoOS.deleteOrdemServico(idOs);
        } catch (Exception e) {
            logger.error("Erro ao excluir Ordem de Serviço", e);
            return false;
        }
    }

    public OrdemServico OrdemServicoById(OrdemServico os) {
        if (os == null) {
            logger.warn("Tentativa de buscar Ordem de Serviço com valor nulo");
            return null;
        }
        try {
            Long idOs = os.getId();
            return daoOS.OrdemServicoById(idOs);
        } catch (Exception e) {
            logger.error("Erro ao buscar Ordem de Serviço com ID: " + os.getId(), e);
            return null;
        }
    }

    public List<OrdemServico> searchOS(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            logger.warn("Busca de Ordem de Serviço com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoOS.searchOS(searchTerm);
        } catch (Exception e) {
            logger.error("Erro ao buscar Ordem de Serviço com pesquisa: " + searchTerm, e);
            return null;
        }
    }
}
