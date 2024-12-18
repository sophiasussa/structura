package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.example.application.model.Material;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Material inserido com sucesso: " + material.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha inserida para o material: " + material.getNome());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao inserir material: " + material.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao inserir material: " + material.getNome(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Material material) {
        String sql = "UPDATE material SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, material.getNome());
            stmt.setInt(2, material.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Material atualizado com sucesso: " + material.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha atualizada para o material com ID: " + material.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao alterar material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao alterar material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Material material) {
        String sql = "DELETE FROM material WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, material.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Material excluído com sucesso: " + material.getId());
                return true;
            } else {
                logger.warn("Nenhuma linha excluída para o material com ID: " + material.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao excluir material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir material com ID: " + material.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
            logger.info("Pesquisados " + lista.size() + " materiais.");
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os materiais.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar todos os materiais.", e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
}

