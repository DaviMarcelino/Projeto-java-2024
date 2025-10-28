package tca.com.repositories;

import tca.com.dao.FuncionarioDAO;
import tca.com.model.Funcionario;

public class FuncionarioRepository {
    private FuncionarioDAO funcionarioDAO;

    public FuncionarioRepository() {
        this.funcionarioDAO = new FuncionarioDAO();
        this.funcionarioDAO.criarTabelaSeNaoExistir(); 
    }

    public Funcionario buscarPorCpf(String cpf) {
        return funcionarioDAO.buscarPorCpf(cpf);
    }
}