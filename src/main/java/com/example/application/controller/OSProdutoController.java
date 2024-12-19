package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.OrdemServico;
import com.example.application.model.ProdutoOS;
import com.example.application.repository.OSProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class OSProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(OSProdutoController.class);
    private OSProdutoRepository daoOSProduto;

    public OSProdutoController() {
        try {
            this.daoOSProduto = new OSProdutoRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public List<ProdutoOS> getProdutoOSByOrdemServicoId(Long ordemServicoId) {
        if (ordemServicoId == null || ordemServicoId <= 0) {
            logger.warn("Busca de ProdutoOS com ID inválido para a Ordem de Serviço");
            return new ArrayList<>();
        }
        try {
            List<ProdutoOS> lista = daoOSProduto.getProdutoOSsByOrdemServicoId(ordemServicoId);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " ProdutoOS para a Ordem de Serviço ID: " + ordemServicoId);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar ProdutoOS com ID da Ordem de Serviço: " + ordemServicoId, e);
            return new ArrayList<>();
        }
    }
}