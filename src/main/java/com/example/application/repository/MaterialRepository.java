package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.application.model.Material;

public class MaterialRepository {

    private static final Logger logger = LoggerFactory.getLogger(MaterialRepository.class);
    private Connection connection;

    public MaterialRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Material material) {
        String sql = "INSERT INTO material (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNome());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir material: " + material.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Material material) {
        String sql = "UPDATE material SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNome());
            stmt.setInt(2, material.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Material material) {
        String sql = "DELETE FROM material WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, material.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Material> pesquisarTodos() {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM material";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Material material = new Material();
                material.setId(resultSet.getInt("id"));
                material.setNome(resultSet.getString("nome"));
                lista.add(material);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os materiais.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public boolean isMaterialInUse(Material material) {
        String sql = "SELECT COUNT(*) FROM produto WHERE material_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, material.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar material em uso.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return false;
    }
}
