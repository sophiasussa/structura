package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cor;

public class DaoCor {
    
    public boolean inserir(Cor cor){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO cor (nome) VALUE (?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, cor.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

        public boolean alterar(Cor cor){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE cor SET nome = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, cor.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Cor cor) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM cor WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, cor.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cor> pesquisarTodos(){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from cor";
            List<Cor> lista = new ArrayList<Cor>();
            Cor cor;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                cor = new Cor();
                cor.setId(resultSet.getInt("id"));
                cor.setNome(resultSet.getString("nome"));
                lista.add(cor);
            }
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
