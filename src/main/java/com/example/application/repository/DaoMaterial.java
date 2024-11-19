package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.example.application.model.Material;

public class DaoMaterial {

    public boolean inserir(Material material){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO material (nome) VALUE (?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, material.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

        public boolean alterar(Material material){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE material SET nome = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, material.getNome());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Material material) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM material WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, material.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Material> pesquisarTodos(){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from material";
            List<Material> lista = new ArrayList<Material>();
            Material material;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                material = new Material();
                material.setId(resultSet.getInt("id"));
                material.setNome(resultSet.getString("nome"));
                lista.add(material);
            }
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
