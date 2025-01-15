package com.example.application.repository;
import com.example.application.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private Connection connection;

    public UserRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password FROM User WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar surname: " + username, e);
            throw new RuntimeException("Erro ao buscar usuário por username.", e);
        }
        return null;
    }

    public User login(String username, String rawPassword) {
        User user = findByUsername(username);

        if (user != null && BCrypt.checkpw(rawPassword, user.getPassword())) {
            return user;
        }

        return null;
    }
}