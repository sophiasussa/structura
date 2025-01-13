package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.repository.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    private FuncionarioRepository daoFuncionario;

    public FuncionarioController() {
        try {
            this.daoFuncionario = new FuncionarioRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public boolean inserir(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de inserir funcionário com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoFuncionario.inserir(funcionario);
            if (sucesso) {
                logger.info("Funcionário inserido com sucesso: " + funcionario.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o funcionário: " + funcionario.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir funcionário: " + funcionario.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de alterar funcionário com valor nulo");
            return false;
        }
        try {
            boolean sucesso = daoFuncionario.alterar(funcionario);
            if (sucesso) {
                logger.info("Funcionário atualizado com sucesso: " + funcionario.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o funcionário com ID: " + funcionario.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar funcionário: " + funcionario.getNome(), e);
            return false;
        }
    }

    public boolean isFuncionarioInUse(Funcionario funcionario) {
        return daoFuncionario.isFuncionarioInUse(funcionario);
    }

    public boolean excluir(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de excluir funcionário com valor nulo");
            return false;
        }else if(isFuncionarioInUse(funcionario)){
            logger.warn("Não é possível excluir o funcionario. O funcionario está associado a um agendamento ou OS");
        }
        try {
            boolean sucesso = daoFuncionario.excluir(funcionario);
            if (sucesso) {
                logger.info("Funcionário excluído com sucesso: " + funcionario.getId());
            } else {
                logger.warn("Nenhuma linha excluída para o funcionário com ID: " + funcionario.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao excluir funcionário com ID: " + funcionario.getId(), e);
            return false;
        }
    }

    public List<Funcionario> pesquisarTodos() {
        try {
            List<Funcionario> lista = daoFuncionario.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " funcionários.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os funcionários", e);
            return new ArrayList<>();
        }
    }

    public Funcionario getFuncionarioById(int id) {
        try {
            Funcionario funcionario = daoFuncionario.getFuncionarioById(id);
            if (funcionario != null) {
                logger.info("Funcionário encontrado pelo ID: " + id);
            } else {
                logger.warn("Nenhum funcionário encontrado com o ID: " + id);
            }
            return funcionario;
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com ID: " + id, e);
            return null;
        }
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de funcionário com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Funcionario> lista = daoFuncionario.pesquisarFuncionario(pesquisa);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " funcionários para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }
}