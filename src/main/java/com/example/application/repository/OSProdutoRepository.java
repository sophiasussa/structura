package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.ProdutoOS;

public class OSProdutoRepository {
    private Connection connection;
    
    public OSProdutoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }
    

    public List<ProdutoOS> getProdutoOSsByOrdemServicoId(long idOs) {
        List<ProdutoOS> osProdutosList = new ArrayList<>();
        String sql = "SELECT * FROM os_produto WHERE id_os = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idOs);
            ResultSet result = stmt.executeQuery();
            
            while (result.next()) {
                ProdutoOS osProduto = new ProdutoOS();
                osProduto.setOrdemServico(new OrdemServicoRepository().OrdemServicoById(result.getLong("id_os")));
                osProduto.setProduto(new ProdutoRepository().getProdutoById(result.getInt("id_produto")));
                osProdutosList.add(osProduto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return osProdutosList;
    }
}
