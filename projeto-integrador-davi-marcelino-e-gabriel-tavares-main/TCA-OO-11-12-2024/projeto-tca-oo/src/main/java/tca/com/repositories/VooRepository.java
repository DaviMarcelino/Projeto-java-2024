package tca.com.repositories;

import java.sql.SQLException;
import java.util.List;
import tca.com.dao.VooDAO;
import tca.com.model.Voo;

public class VooRepository {
    private VooDAO vooDAO = new VooDAO(); 

    public void addFlight(Voo voo) throws SQLException {
        vooDAO.salvar(voo);
    }

    public void updateFlight(Voo voo) throws SQLException {
        vooDAO.atualizar(voo); 
    }

    public void removeFlight(int id) throws SQLException {
        vooDAO.remover(id); 
    }

    public List<Voo> getAllFlights() throws SQLException {
        return vooDAO.buscarTodos(); 
    }
}