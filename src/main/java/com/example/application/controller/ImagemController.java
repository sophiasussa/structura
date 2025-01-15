package com.example.application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.ImagemOS;
import com.example.application.repository.OSImagemRepository;

public class ImagemController {

    private static final Logger logger = LoggerFactory.getLogger(ImagemController.class);
    private OSImagemRepository daoOSProduto;

    public ImagemController() {
        try {
            this.daoOSProduto = new OSImagemRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public List<ImagemOS> getImagemOSByOrdemServicoId(Long ordemServicoId) {
        if (ordemServicoId == null || ordemServicoId <= 0) {
            logger.warn("Busca de ImagemOS com ID inválido para a Ordem de Serviço");
            return new ArrayList<>();
        }
        try {
            List<ImagemOS> lista = daoOSProduto.getImagensByOrdemServicoId(ordemServicoId);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " ImagemOS para a Ordem de Serviço ID: " + ordemServicoId);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar ImagemOS com ID da Ordem de Serviço: " + ordemServicoId, e);
            return new ArrayList<>();
        }
    }
}