package com.example.application.controller;

import java.util.List;

import com.example.application.model.OrdemServico;
import com.example.application.model.ProdutoOS;
import com.example.application.repository.OSProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(OSProdutoController.class);
    private final OSProdutoRepository daoOSProduto;

    public OSProdutoController() {
        try {
            this.daoOSProduto = new OSProdutoRepository();
        } catch (Exception e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public List<ProdutoOS> getProdutoOSByOrdemServicoId(OrdemServico os) {
        if (os == null) {
            logger.warn("Tentativa de buscar ProdutoOS com Ordem de Serviço nula");
            return null;
        }
        try {
            long idOs = os.getId();
            return daoOSProduto.getProdutoOSsByOrdemServicoId(idOs);
        } catch (Exception e) {
            logger.error("Erro ao buscar ProdutoOS com ID da Ordem de Serviço: " + os.getId(), e);
            return null;
        }
    }
}

