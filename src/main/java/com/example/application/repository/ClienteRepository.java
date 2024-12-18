package com.example.application.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import com.example.application.model.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ClienteRepository {

    private static final Logger logger = LoggerFactory.getLogger(ClienteRepository.class);
    private Connection connection;

    public ClienteRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, rg, telefone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setString(4, cliente.getTelefone());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Cliente inserido com sucesso: " + cliente.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha inserida para o cliente: " + cliente.getNome());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao inserir cliente: " + cliente.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao inserir cliente: " + cliente.getNome(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, rg = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setString(4, cliente.getTelefone());
            stmt.setLong(5, cliente.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Cliente atualizado com sucesso: " + cliente.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha atualizada para o cliente com ID: " + cliente.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao alterar cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao alterar cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Cliente excluído com sucesso: " + cliente.getId());
                return true;
            } else {
                logger.warn("Nenhuma linha excluída para o cliente com ID: " + cliente.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao excluir cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Cliente> pesquisarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getString("cpf"));
                cliente.setRg(resultSet.getString("rg"));
                cliente.setTelefone(resultSet.getString("telefone"));
                lista.add(cliente);
            }
            logger.info("Pesquisados " + lista.size() + " clientes.");
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os clientes.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar todos os clientes.", e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public List<Cliente> pesquisarCliente(String pesquisa) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR CAST(cpf AS CHAR) LIKE ? OR CAST(rg AS CHAR) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String busca = "%" + pesquisa + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getString("cpf"));
                cliente.setRg(resultSet.getString("rg"));
                cliente.setTelefone(resultSet.getString("telefone"));
                lista.add(cliente);
            }
            logger.info("Pesquisados " + lista.size() + " clientes para a pesquisa: " + pesquisa);
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar cliente com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar cliente com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public Cliente getClienteById(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getString("cpf"));
                cliente.setRg(resultSet.getString("rg"));
                cliente.setTelefone(resultSet.getString("telefone"));
                logger.info("Cliente encontrado pelo ID: " + id);
                return cliente;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar cliente pelo ID: " + id, e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }
}


