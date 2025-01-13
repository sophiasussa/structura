package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

import com.example.application.model.Material;
import com.example.application.model.Modelo;
public class ModeloRepository {

    private static final Logger logger = LoggerFactory.getLogger(ModeloRepository.class);
    private Connection connection;

    public ModeloRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Modelo modelo) {
        String sql = "INSERT INTO modelo (nome) VALUE (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo.getNome());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir modelo: " + modelo.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo.getNome());
            stmt.setInt(2, modelo.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, modelo.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Modelo> pesquisarTodos() {
        List<Modelo> lista = new ArrayList<>();
        String sql = "SELECT * FROM modelo";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("id"));
                modelo.setNome(resultSet.getString("nome"));
                lista.add(modelo);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os modelos.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public boolean isModeloInUse(Modelo modelo) {
        String sql = "SELECT COUNT(*) FROM produto WHERE modelo_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, modelo.getId());
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

