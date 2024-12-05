package com.example.application.controller;

import java.util.List;

import com.example.application.model.OrdemServico;
import com.example.application.model.ProdutoOS;
import com.example.application.repository.DaoOSProduto;

public class ControllerOSProduto {

    private final DaoOSProduto daoOSProduto;

    public ControllerOSProduto() {
        try {
            this.daoOSProduto = new DaoOSProduto();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public List<ProdutoOS> getProdutoOSByOrdemServicoId(OrdemServico os) {
        try {
            long idOs = os.getId();
            return daoOSProduto.getProdutoOSsByOrdemServicoId(idOs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
