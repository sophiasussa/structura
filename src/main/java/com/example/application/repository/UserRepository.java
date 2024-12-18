package com.example.application.repository;
import com.example.application.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private Connection connection;

    public UserRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("username"), resultSet.getString("password"));
                    logger.info("Usuário encontrado com sucesso: " + username);
                    return user;
                } else {
                    logger.warn("Nenhum usuário encontrado para o username: " + username);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuário com username: " + username, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar usuário com username: " + username, e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }
}