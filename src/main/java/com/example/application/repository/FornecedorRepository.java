package com.example.application.repository;

import java.util.ArrayList;
import java.util.List;
import com.example.application.model.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class FornecedorRepository {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorRepository.class);
    private Connection connection;

    public FornecedorRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (empresa, vendedor, cnpj, ie, telefone, email, descriProdutos) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fornecedor.getEmpresa());
            stmt.setString(2, fornecedor.getVendedor());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.setString(4, fornecedor.getIe());
            stmt.setString(5, fornecedor.getTelefone());
            stmt.setString(6, fornecedor.getEmail());
            stmt.setString(7, fornecedor.getDescriProdutos());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir fornecedor: " + fornecedor.getEmpresa(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET empresa = ?, vendedor = ?, cnpj = ?, ie = ?, telefone = ?, email = ?, descriProdutos = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fornecedor.getEmpresa());
            stmt.setString(2, fornecedor.getVendedor());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.setString(4, fornecedor.getIe());
            stmt.setString(5, fornecedor.getTelefone());
            stmt.setString(6, fornecedor.getEmail());
            stmt.setString(7, fornecedor.getDescriProdutos());
            stmt.setLong(8, fornecedor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar fornecedor com ID: " + fornecedor.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Fornecedor fornecedor) {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, fornecedor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir fornecedor com ID: " + fornecedor.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Fornecedor> pesquisarTodos() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(resultSet.getLong("id"));
                fornecedor.setEmpresa(resultSet.getString("empresa"));
                fornecedor.setVendedor(resultSet.getString("vendedor"));
                fornecedor.setCnpj(resultSet.getString("cnpj"));
                fornecedor.setIe(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdutos(resultSet.getString("descriProdutos"));
                lista.add(fornecedor);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os fornecedores.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public List<Fornecedor> pesquisarFornecedor(String pesquisa) {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor WHERE empresa LIKE ? OR vendedor LIKE ? OR CAST(cnpj AS CHAR) LIKE ? OR CAST(ie AS CHAR) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String busca = "%" + pesquisa + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
            stmt.setString(4, busca);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(resultSet.getLong("id"));
                fornecedor.setEmpresa(resultSet.getString("empresa"));
                fornecedor.setVendedor(resultSet.getString("vendedor"));
                fornecedor.setCnpj(resultSet.getString("cnpj"));
                fornecedor.setIe(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdutos(resultSet.getString("descriProdutos"));
                lista.add(fornecedor);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar fornecedor com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
}