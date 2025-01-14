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
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir cliente: " + cliente.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
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
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir cliente com ID: " + cliente.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
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
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os clientes.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
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
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar cliente com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
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
                return cliente;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }

    public boolean isClienteInUse(Cliente cliente) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM (
                SELECT cliente_id FROM os WHERE cliente_id = ?
                UNION ALL
                SELECT cliente_id FROM projeto WHERE cliente_id = ?
            ) AS combined
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            stmt.setLong(2, cliente.getId());
            ResultSet result = stmt.executeQuery();
    
            if (result.next()) {
                int count = result.getInt("total");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}