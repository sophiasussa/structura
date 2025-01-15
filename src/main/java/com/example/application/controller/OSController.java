package com.example.application.controller;

import com.example.application.model.OrdemServico;
import com.example.application.repository.OrdemServicoRepository;
import com.example.application.model.Produto;
import com.example.application.model.Agenda;
import com.example.application.model.ImagemOS;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSController {

    private static final Logger logger = LoggerFactory.getLogger(OSController.class);
    private final OrdemServicoRepository daoOS;

    public OSController() {
        try {
            this.daoOS = new OrdemServicoRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    private boolean validarOrdemServico(OrdemServico os) {
        if (os == null) {
            logger.warn("Tentativa de inserir Ordem de Serviço com valor nulo");
            return false;
        }
        if (os.getEndereco().length() > 255) {
            logger.warn("Endereço da Ordem de Serviço não pode ter mais de 255 caracteres");
            return false;
        }
        if (os.getCliente() == null) {
            logger.warn("Cliente da Ordem de Serviço é obrigatório");
            return false;
        }
        if (os.getObservacao().length() > 255) {
            logger.warn("Observações da Ordem de Serviço não podem ter mais de 255 caracteres");
            return false;
        }
        return true;
    }

    public Long saveOrdemServico(OrdemServico os, List<Produto> produtos, List<ImagemOS> imagens) {
        if (!validarOrdemServico(os)) {
            return null;
        }
        try {
            return daoOS.saveOrdemServico(os, produtos, imagens);
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
        if (!validarOrdemServico(os)) {
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
            return new ArrayList<>();
        }
    }

    public List<OrdemServico> OrdemServicoPorDataPrevista() {
        try {
            List<OrdemServico> lista = daoOS.OrdemServicoPorDataPrevista();
            logger.info("Pesquisadas " + (lista != null ? lista.size() : 0) + " OSs de hoje.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar OSs de hoje", e);
            return new ArrayList<>();
        }
    }
}