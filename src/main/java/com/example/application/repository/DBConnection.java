package com.example.application.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance = null;
    private Connection conn;

    private final String url = "jdbc:mysql://localhost:3306/strutura";
    private final String usuario = "";
    private final String senha = "";

    private DBConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Erro ao conectar ao banco de dados: " + ex.getMessage());
            throw new SQLException("Falha ao conectar ao banco de dados", ex);
        }
    }

    public Connection getConnection(){
        return conn;
    }

    public static DBConnection getInstance() throws SQLException{
        if(instance == null){
            instance = new DBConnection();
        } else if (instance.getConnection().isClosed()){
            instance = new DBConnection();
        }
        return instance;
    }
}
