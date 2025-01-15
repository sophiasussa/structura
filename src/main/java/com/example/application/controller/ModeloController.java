package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.example.application.model.Modelo;
import com.example.application.repository.ModeloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModeloController {

    private static final Logger logger = LoggerFactory.getLogger(ModeloController.class);
    private ModeloRepository daoModelo;

    public ModeloController() {
        try {
            this.daoModelo = new ModeloRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
    
    private boolean validarModelo(Modelo modelo) {
        if (modelo == null) {
            logger.warn("Tentativa de inserir ou alterar modelo com valor nulo");
            return false;
        }
        if (modelo.getNome() == null || modelo.getNome().isEmpty()) {
            logger.warn("Nome do modelo é obrigatório");
            return false;
        }
        if (modelo.getNome().length() > 100) {
            logger.warn("Nome do modelo não pode ter mais de 100 caracteres");
            return false;
        }
        return true;
    }

    public boolean inserir(Modelo modelo) {
        if (!validarModelo(modelo)) {
            return false;
        }
        try {
            boolean sucesso = daoModelo.inserir(modelo);
            if (sucesso) {
                logger.info("Modelo inserido com sucesso: " + modelo.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o modelo: " + modelo.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir modelo: " + modelo.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        if (!validarModelo(modelo)) {
            return false;
        }
        try {
            boolean sucesso = daoModelo.alterar(modelo);
            if (sucesso) {
                logger.info("Modelo atualizado com sucesso: " + modelo.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o modelo com ID: " + modelo.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar modelo: " + modelo.getNome(), e);
            return false;
        }
    }

    public boolean isModeloInUse(Modelo modelo) {
        try {
            return daoModelo.isModeloInUse(modelo);
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os modelos", e);
            return false;
        }
    }

    public String excluir(Modelo modelo) {
        if (modelo == null) {
            logger.warn("Tentativa de excluir modelo com valor nulo ou ID inválido");
            return "Modelo inválido ou não encontrado.";
        } else if (isModeloInUse(modelo)) {
            logger.warn("Não é possível excluir o modelo. Ele está associado a um produto.");
            return "Não é possível excluir o modelo, pois ele está associado a um produto.";
        }
    
        try {
            boolean sucesso = daoModelo.excluir(modelo);
            if (sucesso) {
                logger.info("Modelo excluído com sucesso: " + modelo.getId());
                return null;
            } else {
                logger.warn("Nenhuma linha excluída para o modelo com ID: " + modelo.getId());
                return "Nenhuma exclusão realizada. Modelo não encontrado.";
            }
        } catch (Exception e) {
            logger.error("Erro ao excluir modelo com ID: " + modelo.getId(), e);
            return "Erro interno ao tentar excluir o modelo. Por favor, entre em contato com o suporte.";
        }
    }
    
    public List<Modelo> pesquisarTodos() {
        try {
            List<Modelo> lista = daoModelo.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " modelos.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os modelos", e);
            return new ArrayList<>();
        }
    }
}