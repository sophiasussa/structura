package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.ProdutoOS;

public class OSProdutoRepository {

    private static final Logger logger = LoggerFactory.getLogger(OSProdutoRepository.class);
    private Connection connection;

    public OSProdutoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public List<ProdutoOS> getProdutoOSsByOrdemServicoId(long idOs) {
        List<ProdutoOS> osProdutosList = new ArrayList<>();
        String sql = "SELECT * FROM produto_os WHERE os_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idOs);
            try (ResultSet result = stmt.executeQuery()) {
                while (result.next()) {
                    ProdutoOS osProduto = new ProdutoOS();
                    osProduto.setOrdemServico(new OrdemServicoRepository().OrdemServicoById(result.getLong("os_id")));
                    osProduto.setProduto(new ProdutoRepository().getProdutoById(result.getInt("produto_id")));
                    osProdutosList.add(osProduto);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar produtos para a ordem de serviço ID: " + idOs, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return osProdutosList;
    }
}