package com.example.application.controller;

import com.example.application.model.OrdemServico;
import com.example.application.repository.DaoOrdemServico;
import com.example.application.model.Produto;
import java.util.List;

public class ControllerOS {
    
    private final DaoOrdemServico daoOS;

    public ControllerOS() {
        try {
            this.daoOS = new DaoOrdemServico();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public Long saveOrdemServico(OrdemServico os) {
        try {
            return daoOS.saveOrdemServico(os);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OrdemServico> getAllOrdensServico() {
        try {
            return daoOS.getAllOrdensServico();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateOrdemServico(OrdemServico os, List<Produto> produtos) {
        try {
            return daoOS.updateOrdemServico(os, produtos);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrdemServico(OrdemServico os) {
        try {
            Long idOs = os.getId();
            return daoOS.deleteOrdemServico(idOs);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public OrdemServico OrdemServicoById(OrdemServico os) {
        try {
            Long idOs = os.getId();
            return daoOS.OrdemServicoById(idOs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OrdemServico> searchOS(String searchTerm) {
        return daoOS.searchOS(searchTerm);
    }
}
