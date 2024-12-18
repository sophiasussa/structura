package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

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
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Modelo inserido com sucesso: " + modelo.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha inserida para o modelo: " + modelo.getNome());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao inserir modelo: " + modelo.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao inserir modelo: " + modelo.getNome(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo.getNome());
            stmt.setInt(2, modelo.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Modelo atualizado com sucesso: " + modelo.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha atualizada para o modelo com ID: " + modelo.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao alterar modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao alterar modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, modelo.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Modelo excluído com sucesso: " + modelo.getId());
                return true;
            } else {
                logger.warn("Nenhuma linha excluída para o modelo com ID: " + modelo.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao excluir modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir modelo com ID: " + modelo.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
            logger.info("Pesquisados " + lista.size() + " modelos.");
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os modelos.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar todos os modelos.", e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
}

