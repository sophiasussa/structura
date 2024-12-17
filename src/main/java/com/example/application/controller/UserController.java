package com.example.application.controller;
import com.example.application.model.User;
import com.example.application.repository.DaoUser;
import java.sql.SQLException;

public class UserController {
    private DaoUser userRepository;

    public UserController() {
        try {
            this.userRepository = new DaoUser();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
