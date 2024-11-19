package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.UnidMedida;

public class DaoUnidMedida {

    public boolean inserir(UnidMedida unidMedida){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO unidMedida (nome) VALUE (?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, unidMedida.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

        public boolean alterar(UnidMedida unidMedida){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE unidMedida SET nome = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, unidMedida.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(UnidMedida unidMedida) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM unidMedida WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, unidMedida.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<UnidMedida> pesquisarTodos(){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from unidMedida";
            List<UnidMedida> lista = new ArrayList<UnidMedida>();
            UnidMedida unidMedida;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("id"));
                unidMedida.setNome(resultSet.getString("nome"));
                lista.add(unidMedida);
            }
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
