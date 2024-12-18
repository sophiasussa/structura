package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorRepository {

    private static final Logger logger = LoggerFactory.getLogger(CorRepository.class);
    private Connection connection;

    public CorRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Cor cor) {
        String sql = "INSERT INTO cor (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cor.getNome());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Cor inserida com sucesso: " + cor.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha inserida para a cor: " + cor.getNome());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao inserir cor: " + cor.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao inserir cor: " + cor.getNome(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Cor cor) {
        String sql = "UPDATE cor SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cor.getNome());
            stmt.setInt(2, cor.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Cor atualizada com sucesso: " + cor.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha atualizada para a cor com ID: " + cor.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao alterar cor com ID: " + cor.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao alterar cor com ID: " + cor.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Cor cor) {
        String sql = "DELETE FROM cor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cor.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Cor excluída com sucesso: " + cor.getId());
                return true;
            } else {
                logger.warn("Nenhuma linha excluída para a cor com ID: " + cor.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao excluir cor com ID: " + cor.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir cor com ID: " + cor.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Cor> pesquisarTodos() {
        List<Cor> lista = new ArrayList<>();
        String sql = "SELECT * FROM cor";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Cor cor = new Cor();
                cor.setId(resultSet.getInt("id"));
                cor.setNome(resultSet.getString("nome"));
                lista.add(cor);
            }
            logger.info("Pesquisadas " + lista.size() + " cores.");
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todas as cores.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar todas as cores.", e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
}