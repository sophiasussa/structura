package com.example.application.repository;
import com.example.application.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class UserRepository {
    private Connection connection;
    
    public UserRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            String query = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
