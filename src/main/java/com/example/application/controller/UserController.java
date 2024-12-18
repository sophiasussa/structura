package com.example.application.controller;
import com.example.application.model.User;
import com.example.application.repository.UserRepository;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserRepository userRepository;

    public UserController() {
        try {
            this.userRepository = new UserRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public User login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            logger.warn("Tentativa de login com parâmetros inválidos (username ou password nulo/ vazio)");
            return null;
        }
        try {
            return userRepository.findByUsernameAndPassword(username, password);
        } catch (Exception e) {
            logger.error("Erro ao realizar login com username: " + username, e);
            return null;
        }
    }
}
