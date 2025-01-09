package com.example.application.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.ImagemOS;

public class OSImagemRepository {

    private static final Logger logger = LoggerFactory.getLogger(OSImagemRepository.class);
    private Connection connection;

    public OSImagemRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public List<ImagemOS> getImagensByOrdemServicoId(long idOs) {
        List<ImagemOS> imagensList = new ArrayList<>();
        String sql = "SELECT * FROM os_imagens WHERE os_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idOs);
            try (ResultSet result = stmt.executeQuery()) {
                while (result.next()) {
                    ImagemOS imagemOS = new ImagemOS();
                    imagemOS.setId(result.getLong("id"));
                    imagemOS.setOrdemServico(new OrdemServicoRepository().OrdemServicoById(result.getLong("os_id")));
                    imagemOS.setCaminhoImagem(result.getString("caminho_imagem"));
                    imagensList.add(imagemOS);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar imagens para a ordem de serviço ID: " + idOs, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return imagensList;
    }
}
