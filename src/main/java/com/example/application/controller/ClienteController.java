package com.example.application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private ClienteRepository daoCliente;

    public ClienteController() {
        try {
            this.daoCliente = new ClienteRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Cliente cliente) {
        if (cliente == null) {
            logger.warn("Tentativa de inserir cliente com valor nulo");
            return false;
        }
        try {
            return daoCliente.inserir(cliente);
        } catch (Exception e) {
            logger.error("Erro ao inserir cliente", e);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        if (cliente == null) {
            logger.warn("Tentativa de alterar cliente com valor nulo");
            return false;
        }
        try {
            return daoCliente.alterar(cliente);
        } catch (Exception e) {
            logger.error("Erro ao alterar cliente", e);
            return false;
        }
    }

    public boolean excluir(Cliente cliente) {
        if (cliente == null) {
            logger.warn("Tentativa de excluir cliente com valor nulo");
            return false;
        }
        try {
            return daoCliente.excluir(cliente);
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente", e);
            return false;
        }
    }

    public List<Cliente> pesquisarTodos() {
        try {
            return daoCliente.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os clientes", e);
            return null;
        }
    }

    public List<Cliente> pesquisarCliente(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de cliente com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoCliente.pesquisarCliente(pesquisa);
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente com pesquisa: " + pesquisa, e);
            return null;
        }
    }

    public Cliente getClienteById(int id) {
        try {
            return daoCliente.getClienteById(id);
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente com ID: " + id, e);
            return null;
        }
    }
}
