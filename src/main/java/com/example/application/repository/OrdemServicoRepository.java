package com.example.application.repository;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


import com.example.application.model.EntregaOS;
import com.example.application.model.Material;
import com.example.application.model.OrdemServico;
import com.example.application.model.Produto;
import com.example.application.model.StatusOS;

public class OrdemServicoRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoRepository.class);
    private Connection connection;
    
    public OrdemServicoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public long saveOrdemServico(OrdemServico os, List<Produto> produtos) throws SQLException {
        String sqlOS = "INSERT INTO os (status_os, entrega_os, endereco, dataa, datap, observacoes, cliente_id, funcionario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlOsProdutos = "INSERT INTO produto_os (os_id, produto_id) VALUES (?, ?)";
        String sqlOsImagens = "INSERT INTO os_imagens (os_id, caminho_imagem) VALUES (?, ?)";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtOS.setString(1, os.getStatusOS().name());
            stmtOS.setString(2, os.getEntregaOS().name());
            stmtOS.setString(3, os.getEndereco());
            stmtOS.setDate(4, os.getDataAbertura() != null ? java.sql.Date.valueOf(os.getDataAbertura()) : null);
            stmtOS.setDate(5, os.getDataPrevFinaliza() != null ? java.sql.Date.valueOf(os.getDataPrevFinaliza()) : null);
            stmtOS.setString(6, os.getObservacao());

            if (os.getCliente() == null || os.getCliente().getId() == null) {
                throw new IllegalArgumentException("O cliente ou o ID do cliente não pode ser nulo.");
            }
            stmtOS.setLong(7, os.getCliente().getId());

            if (os.getFuncionario() == null || os.getFuncionario().getId() == null) {
                throw new IllegalArgumentException("O funcionario ou o ID do funcionario não pode ser nulo.");
            }
            stmtOS.setLong(8, os.getFuncionario().getId());

            int rowsInserted = stmtOS.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmtOS.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idOs = generatedKeys.getLong(1);

                        if (produtos != null && !produtos.isEmpty()) {
                            try (PreparedStatement stmtOsProdutos = connection.prepareStatement(sqlOsProdutos)) {
                                for (Produto produto : produtos) {
                                    stmtOsProdutos.setLong(1, idOs);
                                    stmtOsProdutos.setLong(2, produto.getId());
                                    stmtOsProdutos.addBatch();
                                }
                                stmtOsProdutos.executeBatch();
                            }
                        }
                        return idOs;
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            logger.error("Erro ao salvar Ordem de Serviço", e);
            throw new RuntimeException("Erro ao salvar Ordem de Serviço", e);
        }
    }
    
    public boolean updateOrdemServico(OrdemServico os, List<Produto> produtos) {
        String sqlOS = "UPDATE os SET status_os = ?, entrega_os = ?, endereco = ?, dataa = ?, datap = ?, observacoes = ?, cliente_id = ?, funcionario_id = ? WHERE id = ?";
        String deleteOsProdutos = "DELETE FROM produto_os WHERE os_id = ?";
        String insertOsProdutos = "INSERT INTO produto_os (os_id, produto_id) VALUES (?, ?)";
    
        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS)) {
            stmtOS.setString(1, os.getStatusOS().name());
            stmtOS.setString(2, os.getEntregaOS().name());
            stmtOS.setString(3, os.getEndereco());
            stmtOS.setDate(4, os.getDataAbertura() != null ? java.sql.Date.valueOf(os.getDataAbertura()) : null);
            stmtOS.setDate(5, os.getDataPrevFinaliza() != null ? java.sql.Date.valueOf(os.getDataPrevFinaliza()) : null);
            stmtOS.setString(6, os.getObservacao());

            if (os.getCliente() == null || os.getCliente().getId() == null) {
                throw new IllegalArgumentException("O cliente ou o ID do cliente não pode ser nulo.");
            }
            stmtOS.setLong(7, os.getCliente().getId());

            if (os.getFuncionario() == null || os.getFuncionario().getId() == null) {
                throw new IllegalArgumentException("O funcionario ou o ID do funcionario não pode ser nulo.");
            }
            stmtOS.setLong(8, os.getFuncionario().getId());
    
            if (os.getId() == null) {
                throw new IllegalArgumentException("ID da OS não pode ser nulo para a atualização.");
            }
            stmtOS.setLong(9, os.getId());
    
            int rowsUpdated = stmtOS.executeUpdate();
    
            if (rowsUpdated > 0) {
                try (PreparedStatement stmtDeleteProduto = connection.prepareStatement(deleteOsProdutos)) {
                    stmtDeleteProduto.setLong(1, os.getId());
                    stmtDeleteProduto.executeUpdate();
                }
    
                if (produtos != null && !produtos.isEmpty()) {
                    try (PreparedStatement stmtInsertProduto = connection.prepareStatement(insertOsProdutos)) {
                        for (Produto produto : produtos) {
                            if (produto != null && produto.getId() != null) {
                                stmtInsertProduto.setLong(1, os.getId());
                                stmtInsertProduto.setLong(2, produto.getId());
                                stmtInsertProduto.addBatch();
                            }
                        }
                        stmtInsertProduto.executeBatch();
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Erro ao atualizar Ordem de Serviço com ID: " + os.getId(), e);
            throw new RuntimeException("Erro ao atualizar Ordem de Serviço", e);
        }
    }
    

    public boolean deleteOrdemServico(Long idOs) {
        String sqlDeleteProdutos = "DELETE FROM produto_os WHERE os_id = ?";
        String sqlDeleteOS = "DELETE FROM os WHERE id = ?";
    
        try (PreparedStatement stmtDeleteProdutos = connection.prepareStatement(sqlDeleteProdutos);
             PreparedStatement stmtDeleteOS = connection.prepareStatement(sqlDeleteOS)) {

            stmtDeleteProdutos.setLong(1, idOs);
            stmtDeleteProdutos.executeUpdate();

            stmtDeleteOS.setLong(1, idOs);
            int rowsDeleted = stmtDeleteOS.executeUpdate();
    
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir Ordem de Serviço com ID: " + idOs, e);
            throw new RuntimeException("Erro ao excluir Ordem de Serviço", e);
        }
    }
    

    public List<OrdemServico> getAllOrdensServico() {
        List<OrdemServico> ordens = new ArrayList<>();
        String sqlOS = "SELECT * FROM os";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS);
            ResultSet rsOS = stmtOS.executeQuery()) {

            while (rsOS.next()) {
                OrdemServico os = new OrdemServico();
                os.setId(rsOS.getLong("id"));
                String status = rsOS.getString("status_os");
                os.setStatusOS(status != null ? StatusOS.valueOf(status) : null);
                String entrega = rsOS.getString("entrega_os");
                os.setEntregaOS(entrega != null ? EntregaOS.valueOf(entrega) : null);
                os.setEndereco(rsOS.getString("endereco"));
                java.sql.Date dataSql = rsOS.getDate("dataa");
                java.sql.Date datapSql = rsOS.getDate("datap");
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(datapSql != null ? datapSql.toLocalDate() : null);
                os.setObservacao(rsOS.getString("observacoes"));
                os.setCliente(new ClienteRepository().getClienteById(rsOS.getInt("cliente_id")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(rsOS.getInt("funcionario_id")));
                
                ordens.add(os);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos as OS: ", e);
            throw new RuntimeException("Erro ao pesquisar todos as OS", e);
        }
        return ordens;
    }

    public List<OrdemServico> searchOS(String searchTerm) {
        List<OrdemServico> ordens = new ArrayList<>();
        String sqlOS = "SELECT os.*, c.nome AS cliente_nome FROM os " +
                    "JOIN cliente c ON os.cliente_id = c.id " +
                    "WHERE (os.id = ? OR c.nome LIKE ? OR os.status_os LIKE ?)";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS)) {
            try {
                long idSearch = Long.parseLong(searchTerm);
                stmtOS.setLong(1, idSearch);
            } catch (NumberFormatException e) {
                stmtOS.setLong(1, -1);
            }

            stmtOS.setString(2, "%" + searchTerm + "%");
            stmtOS.setString(3, "%" + searchTerm + "%");

            try (ResultSet rsOS = stmtOS.executeQuery()) {
                while (rsOS.next()) {
                    OrdemServico os = new OrdemServico();
                    os.setId(rsOS.getLong("id"));
                    String status = rsOS.getString("status_os");
                    os.setStatusOS(status != null ? StatusOS.valueOf(status) : null);
                    String entrega = rsOS.getString("entrega_os");
                    os.setEntregaOS(entrega != null ? EntregaOS.valueOf(entrega) : null);
                    os.setEndereco(rsOS.getString("endereco"));
                    java.sql.Date dataSql = rsOS.getDate("dataa");
                    os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                    os.setDataPrevFinaliza(rsOS.getDate("datap") != null ? rsOS.getDate("datap").toLocalDate() : null);
                    os.setObservacao(rsOS.getString("observacoes"));
                    os.setCliente(new ClienteRepository().getClienteById(rsOS.getInt("cliente_id")));
                    os.setFuncionario(new FuncionarioRepository().getFuncionarioById(rsOS.getInt("funcionario_id")));
                    ordens.add(os);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar a OS: ", e);
            throw new RuntimeException("Erro ao pesquisar a OS", e);
        }
        return ordens;
    }

    public OrdemServico OrdemServicoById(Long id) {
        String sql = "SELECT os.*, c.*, m.*" +
                "FROM os " +
                "LEFT JOIN cliente c ON os.cliente_id = c.id " +
                "LEFT JOIN funcionario m ON os.funcionario_id = m.id " +
                "WHERE os.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                OrdemServico os = new OrdemServico();
                os.setStatusOS(StatusOS.valueOf(result.getString("status_os")));
                os.setEntregaOS(EntregaOS.valueOf(result.getString("entrega_os")));
                os.setEndereco(result.getString("endereco"));
                java.sql.Date dataSql = result.getDate("dataa");
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(result.getString("observacoes"));
                os.setCliente(new ClienteRepository().getClienteById(result.getInt("cliente_id")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(result.getInt("funcionario_id")));

                return os;
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar a OS: ", e);
            throw new RuntimeException("Erro ao pesquisar a OS", e);
        }
    }

    public List<OrdemServico> OrdemServicoPorDataPrevista() {
        String sql = "SELECT os.*, c.*, m.* " +
                     "FROM os " +
                     "LEFT JOIN cliente c ON os.cliente_id = c.id " +
                     "LEFT JOIN funcionario m ON os.funcionario_id = m.id " +
                     "WHERE os.datap = CURDATE()";
    
        List<OrdemServico> ordensServico = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
    
            while (result.next()) {
                OrdemServico os = new OrdemServico();

                os.setId(result.getLong("id"));
                os.setStatusOS(StatusOS.valueOf(result.getString("status_os")));
                os.setEntregaOS(EntregaOS.valueOf(result.getString("entrega_os")));
                os.setEndereco(result.getString("endereco"));
                
                java.sql.Date dataSql = result.getDate("dataa");
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(dataSql != null ? dataSql.toLocalDate() : null);
                
                os.setObservacao(result.getString("observacoes"));
                os.setCliente(new ClienteRepository().getClienteById(result.getInt("cliente_id")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(result.getInt("funcionario_id")));
    
                ordensServico.add(os);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar a OS pela data: ", e);
            throw new RuntimeException("Erro ao pesquisar a OS pela data", e);
        }
        return ordensServico;
    }
}