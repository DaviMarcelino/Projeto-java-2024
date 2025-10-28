package tca.com.repositories;

import tca.com.dao.PassageiroDAO;
import tca.com.model.Passageiro;

public class PassageiroRepository {
    private PassageiroDAO passageiroDAO;

    public PassageiroRepository() {
        this.passageiroDAO = new PassageiroDAO();
        this.passageiroDAO.criarTabelaSeNaoExistir(); 
    }

    public void salvar(Passageiro passageiro) {
        passageiroDAO.salvar(passageiro); 
    }

    public Passageiro buscarPorCpf(String cpf) {
        return passageiroDAO.buscarPorCpf(cpf); 
    }
}