package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Modelo;

public class DaoModelo {
    public boolean inserir(Modelo modelo){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO modelo (nome) VALUE (?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, modelo.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

        public boolean alterar(Modelo modelo){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE modelo SET nome = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, modelo.getNome());
            prepareStatement.setInt(2, modelo.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Modelo modelo) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM modelo WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, modelo.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Modelo> pesquisarTodos(){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from modelo";
            List<Modelo> lista = new ArrayList<Modelo>();
            Modelo modelo;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                modelo = new Modelo();
                modelo.setId(resultSet.getInt("id"));
                modelo.setNome(resultSet.getString("nome"));
                lista.add(modelo);
            }
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
