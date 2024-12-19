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
            boolean sucesso = daoCliente.inserir(cliente);
            if (sucesso) {
                logger.info("Cliente inserido com sucesso: " + cliente.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o cliente: " + cliente.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir cliente: " + cliente.getNome(), e);
            return false;
        }
    }
    
    public boolean alterar(Cliente cliente) {
        if (cliente == null) {
            logger.warn("Tentativa de alterar cliente com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoCliente.alterar(cliente);
            if (sucesso) {
                logger.info("Cliente atualizado com sucesso: " + cliente.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o cliente com ID: " + cliente.getId());
            }
            return sucesso;
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
            boolean sucesso = daoCliente.excluir(cliente);
            if (sucesso) {
                logger.info("Cliente excluído com sucesso: " + cliente.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o cliente com ID: " + cliente.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente", e);
            return false;
        }
    }

    public List<Cliente> pesquisarTodos() {
        try {
            List<Cliente> lista = daoCliente.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " clientes.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os clientes", e);
            return new ArrayList<>();
        }
    }
    
    public List<Cliente> pesquisarCliente(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de cliente com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Cliente> lista = daoCliente.pesquisarCliente(pesquisa);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " clientes para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }
    
    public Cliente getClienteById(int id) {
        try {
            Cliente cliente = daoCliente.getClienteById(id);
            if (cliente != null) {
                logger.info("Cliente encontrado pelo ID: " + id);
            } else {
                logger.warn("Nenhum cliente encontrado com o ID: " + id);
            }
            return cliente;
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente com ID: " + id, e);
            return null;
        }
    }
}