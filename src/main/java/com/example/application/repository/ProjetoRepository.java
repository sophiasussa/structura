package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.Cliente;
import com.example.application.model.Projeto;
import com.example.application.model.StatusProjeto;

public class ProjetoRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProjetoRepository.class);
    private Connection connection;

    public ProjetoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Projeto projeto) {
        String sql = "INSERT INTO projeto (status_pr, data, valor, desconto, valor_final, descricao, medidas, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getStatusPr().name());
            stmt.setDate(2, projeto.getData() != null ? java.sql.Date.valueOf(projeto.getData()) : null);
            stmt.setDouble(3, projeto.getValor());
            stmt.setDouble(4, projeto.getDesconto());
            stmt.setDouble(5, projeto.getValorFinal());
            stmt.setString(6, projeto.getDescricao());
            stmt.setString(7, projeto.getMedidas());
            stmt.setLong(8, projeto.getCliente().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir projeto: " + projeto.getDescricao(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Projeto projeto) {
        String sql = "UPDATE projeto SET status_pr = ?, data = ?, valor = ?, desconto = ?, valor_final = ?, descricao = ?, medidas = ?, cliente_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getStatusPr().name());
            stmt.setDate(2, projeto.getData() != null ? java.sql.Date.valueOf(projeto.getData()) : null);
            stmt.setDouble(3, projeto.getValor());
            stmt.setDouble(4, projeto.getDesconto());
            stmt.setDouble(5, projeto.getValorFinal());
            stmt.setString(6, projeto.getDescricao());
            stmt.setString(7, projeto.getMedidas());
            stmt.setLong(8, projeto.getCliente().getId());
            stmt.setLong(9, projeto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar projeto com ID: " + projeto.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Projeto projeto) {
        String sql = "DELETE FROM projeto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projeto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir projeto com ID: " + projeto.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Projeto> pesquisarTodos() {
        List<Projeto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS cliente_nome FROM projeto p LEFT JOIN cliente c ON p.cliente_id = c.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
    
            while (resultSet.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(resultSet.getLong("id"));
                projeto.setStatusPr(StatusProjeto.valueOf(resultSet.getString("status_pr")));
                projeto.setData(resultSet.getDate("data") != null ? resultSet.getDate("data").toLocalDate() : null);
                projeto.setValor(resultSet.getDouble("valor"));
                projeto.setDesconto(resultSet.getDouble("desconto"));
                projeto.setValorFinal(resultSet.getDouble("valor_final"));
                projeto.setDescricao(resultSet.getString("descricao"));
                projeto.setMedidas(resultSet.getString("medidas"));
    
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("cliente_id"));
                cliente.setNome(resultSet.getString("cliente_nome"));
                projeto.setCliente(cliente);
    
                lista.add(projeto);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os projetos.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
    
    public List<Projeto> pesquisarProjeto(String pesquisa) {
        List<Projeto> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, c.nome AS cliente_nome
            FROM projeto p
            LEFT JOIN cliente c ON p.cliente_id = c.id
            WHERE c.nome LIKE ?
            OR p.status_pr LIKE ?
            OR DATE_FORMAT(p.data, '%d/%m/%Y') LIKE ?
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String busca = "%" + pesquisa + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
    
            ResultSet resultSet = stmt.executeQuery();
    
            while (resultSet.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(resultSet.getLong("id"));
                projeto.setStatusPr(StatusProjeto.valueOf(resultSet.getString("status_pr")));
                projeto.setData(resultSet.getDate("data") != null ? resultSet.getDate("data").toLocalDate() : null);
                projeto.setValor(resultSet.getDouble("valor"));
                projeto.setDesconto(resultSet.getDouble("desconto"));
                projeto.setValorFinal(resultSet.getDouble("valor_final"));
                projeto.setDescricao(resultSet.getString("descricao"));
                projeto.setMedidas(resultSet.getString("medidas"));
    
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("cliente_id"));
                cliente.setNome(resultSet.getString("cliente_nome"));
                projeto.setCliente(cliente);
    
                lista.add(projeto);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar projeto com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
    
    public Projeto getProjetoById(long id) {
        String sql = "SELECT * FROM projeto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(resultSet.getLong("id"));
                projeto.setStatusPr(StatusProjeto.valueOf(resultSet.getString("status_pr")));
                projeto.setData(resultSet.getDate("data") != null ? resultSet.getDate("data").toLocalDate() : null);
                projeto.setValor(resultSet.getDouble("valor"));
                projeto.setDesconto(resultSet.getDouble("desconto"));
                projeto.setValorFinal(resultSet.getDouble("valor_final"));
                projeto.setDescricao(resultSet.getString("descricao"));
                projeto.setMedidas(resultSet.getString("medidas"));
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("cliente_id"));
                projeto.setCliente(cliente);
                return projeto;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar projeto pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }
}