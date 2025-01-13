package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

import com.example.application.model.UnidMedida;
public class UnidMedidaRepository {

    private static final Logger logger = LoggerFactory.getLogger(UnidMedidaRepository.class);
    private Connection connection;

    public UnidMedidaRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(UnidMedida unidMedida) {
        String sql = "INSERT INTO unidMedida (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unidMedida.getNome());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir Unidade de Medida: " + unidMedida.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(UnidMedida unidMedida) {
        String sql = "UPDATE unidMedida SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unidMedida.getNome());
            stmt.setInt(2, unidMedida.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar Unidade de Medida com ID: " + unidMedida.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(UnidMedida unidMedida) {
        String sql = "DELETE FROM unidMedida WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, unidMedida.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir Unidade de Medida com ID: " + unidMedida.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<UnidMedida> pesquisarTodos() {
        List<UnidMedida> lista = new ArrayList<>();
        String sql = "SELECT * FROM unidMedida";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("id"));
                unidMedida.setNome(resultSet.getString("nome"));
                lista.add(unidMedida);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todas as unidades de medida.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public boolean isUnidMedidaInUse(UnidMedida unidMedida) {
        String sql = "SELECT COUNT(*) FROM produto WHERE unidMedida_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unidMedida.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
