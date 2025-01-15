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

    private boolean validarCliente(Cliente cliente) {
        if (cliente == null) {
            logger.warn("Tentativa de inserir cliente com valor nulo");
            return false;
        }
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            logger.warn("Nome do cliente é obrigatório");
            return false;
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isEmpty()) {
            logger.warn("Telefone do cliente é obrigatório");
            return false;
        }
        if (!cliente.getTelefone().matches("\\(\\d{2}\\) \\d{4,5}-\\d{4}")) {
            logger.warn("Telefone do cliente com formato inválido: " + cliente.getTelefone());
            return false;
        }
        return true;
    }

    public boolean inserir(Cliente cliente) {
        if (!validarCliente(cliente)) {
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
        if (!validarCliente(cliente)) {
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

    public boolean isClienteInUse(Cliente cliente) {
        try {
            return daoCliente.isClienteInUse(cliente);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes em uso", e);
            return false;
        }
    }

    public String excluir(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            logger.warn("Tentativa de excluir cliente com valor nulo ou ID inválido");
            return "Cliente inválido ou não encontrado.";
        } else if (isClienteInUse(cliente)) {
            logger.warn("Não é possível excluir o cliente. O cliente está associado a uma OS ou projeto");
            return "Não é possível excluir o cliente, pois ele está associado a uma ordem de serviço ou projeto.";
        }
        try {
            boolean sucesso = daoCliente.excluir(cliente);
            if (sucesso) {
                logger.info("Cliente excluído com sucesso: " + cliente.getId());
                return null;
            } else {
                logger.warn("Nenhuma linha excluída para o cliente com ID: " + cliente.getId());
                return "Nenhuma exclusão realizada. Cliente não encontrado.";
            }
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente", e);
            return "Erro interno ao tentar excluir o cliente. Por favor, entre em contato com o suporte.";
        }
    }
    

    public List<Cliente> pesquisarTodos() {
        try {
            List<Cliente> lista = daoCliente.pesquisarTodos();
            if (lista == null) {
                lista = new ArrayList<>();
            }
            logger.info("Pesquisados " + lista.size() + " clientes.");
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
            if (lista == null) {
                lista = new ArrayList<>();
            }
            logger.info("Pesquisados " + lista.size() + " clientes para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }
    
    public Cliente getClienteById(int id) {
        if (id <= 0) {
            logger.warn("ID de cliente inválido: " + id);
            return null;
        }
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