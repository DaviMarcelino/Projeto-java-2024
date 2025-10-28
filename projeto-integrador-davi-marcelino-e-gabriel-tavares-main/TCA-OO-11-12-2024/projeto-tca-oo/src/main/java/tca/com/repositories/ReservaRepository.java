package tca.com.repositories;

import tca.com.dao.ReservaDAO;
import tca.com.model.Reserva;

import java.sql.SQLException;
import java.util.List;

public class ReservaRepository {
    private ReservaDAO reservaDAO;

    public ReservaRepository() {
        this.reservaDAO = new ReservaDAO();
    }

    public void save(Reserva reserva) throws SQLException {
        reservaDAO.salvar(reserva); 
    }

    public void remove(int id) throws SQLException {
        reservaDAO.remover(id); 
    }

    public List<Reserva> findAll() throws SQLException {
        return reservaDAO.buscarTodas(); 
    }

    public Reserva findById(int id) throws SQLException {
        return reservaDAO.buscarPorId(id); 
    }

    public void update(Reserva reserva) throws SQLException {
        reservaDAO.atualizar(reserva); 
    }
}