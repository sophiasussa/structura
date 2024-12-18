package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.repository.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    private FuncionarioRepository daoFuncionario;

    public FuncionarioController() {
        try {
            this.daoFuncionario = new FuncionarioRepository();
        } catch (Exception e) {
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
            return daoFuncionario.inserir(funcionario);
        } catch (Exception e) {
            logger.error("Erro ao inserir funcionário", e);
            return false;
        }
    }

    public boolean alterar(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de alterar funcionário com valor nulo");
            return false;
        }
        try {
            return daoFuncionario.alterar(funcionario);
        } catch (Exception e) {
            logger.error("Erro ao alterar funcionário", e);
            return false;
        }
    }

    public boolean excluir(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de excluir funcionário com valor nulo");
            return false;
        }
        try {
            return daoFuncionario.excluir(funcionario);
        } catch (Exception e) {
            logger.error("Erro ao excluir funcionário", e);
            return false;
        }
    }

    public List<Funcionario> pesquisarTodos() {
        try {
            return daoFuncionario.pesquisarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os funcionários", e);
            return null;
        }
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de funcionário com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            return daoFuncionario.pesquisarFuncionario(pesquisa);
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com pesquisa: " + pesquisa, e);
            return null;
        }
    }

    public Funcionario getFuncionarioById(int id) {
        try {
            return daoFuncionario.getFuncionarioById(id);
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com ID: " + id, e);
            return null;
        }
    }
}
