package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.example.application.model.Cliente;

public class DaoPedra {

    public boolean inserir(Pedra cliente){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO cliente (nome, cpf, rg, telefone) VALUE (?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, cliente.getNome());
            prepareStatement.setString(2, cliente.getCpf());
            prepareStatement.setString(3, cliente.getRg());
            prepareStatement.setString(4, cliente.getTelefone());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
