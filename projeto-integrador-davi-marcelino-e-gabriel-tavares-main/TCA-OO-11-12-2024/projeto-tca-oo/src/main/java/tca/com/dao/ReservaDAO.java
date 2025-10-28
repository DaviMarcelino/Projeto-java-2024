package tca.com.dao;

import tca.com.BD.DatabaseConnection;
import tca.com.model.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaDAO {
    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS Reserva (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "idPassageiro INT, " +
                "idVoo INT, " +
                "dataReserva DATETIME, " +
                "FOREIGN KEY (idPassageiro) REFERENCES Passageiro(id), " +
                "FOREIGN KEY (idVoo) REFERENCES Voo(id))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Reserva reserva) {
        String sql = "INSERT INTO Reserva (idPassageiro, idVoo, dataReserva) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getIdPassageiro());
            stmt.setInt(2, reserva.getIdVoo());
            stmt.setObject(3, reserva.getDataReserva());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reserva> buscarTodas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.id, r.idPassageiro, r.idVoo, r.dataReserva, p.nome AS nomePassageiro, p.cpf AS cpfPassageiro, v.localPartida AS origem, v.localChegada AS chegada " +
                "FROM Reserva r " +
                "JOIN Passageiro p ON r.idPassageiro = p.id " +
                "JOIN Voo v ON r.idVoo = v.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setIdPassageiro(rs.getInt("idPassageiro"));
                reserva.setIdVoo(rs.getInt("idVoo"));
                reserva.setDataReserva(rs.getTimestamp("dataReserva").toLocalDateTime());
                reserva.setNomePassageiro(rs.getString("nomePassageiro"));
                reserva.setCpfPassageiro(rs.getString("cpfPassageiro"));
                reserva.setOrigem(rs.getString("origem"));
                reserva.setChegada(rs.getString("chegada"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public Reserva buscarPorId(int id) {
        Reserva reserva = null;
        String sql = "SELECT * FROM Reserva WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reserva = new Reserva();
                    reserva.setId(rs.getInt("id"));
                    reserva.setIdPassageiro(rs.getInt("idPassageiro"));
                    reserva.setIdVoo(rs.getInt("idVoo"));
                    reserva.setDataReserva(rs.getTimestamp("dataReserva").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reserva;
    }

    public void atualizar(Reserva reserva) {
        String sql = "UPDATE Reserva SET idPassageiro = ?, idVoo = ?, dataReserva = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getIdPassageiro());
            stmt.setInt(2, reserva.getIdVoo());
            stmt.setObject(3, reserva.getDataReserva());
            stmt.setInt(4, reserva.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM Reserva WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reserva> buscarReservas(String termoBusca) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.id, r.idPassageiro, r.idVoo, r.dataReserva, p.nome AS nomePassageiro, p.cpf AS cpfPassageiro, v.localPartida AS origem, v.localChegada AS chegada " +
                "FROM Reserva r " +
                "JOIN Passageiro p ON r.idPassageiro = p.id " +
                "JOIN Voo v ON r.idVoo = v.id " +
                "WHERE r.id LIKE ? OR p.nome LIKE ? OR p.cpf LIKE ? OR r.idVoo LIKE ? OR v.localPartida LIKE ? OR v.localChegada LIKE ? OR r.dataReserva LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String padraoBusca = "%" + termoBusca + "%";
            stmt.setString(1, padraoBusca); 
            stmt.setString(2, padraoBusca); 
            stmt.setString(3, padraoBusca); 
            stmt.setString(4, padraoBusca); 
            stmt.setString(5, padraoBusca); 
            stmt.setString(6, padraoBusca); 
            stmt.setString(7, padraoBusca); 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setId(rs.getInt("id"));
                    reserva.setIdPassageiro(rs.getInt("idPassageiro"));
                    reserva.setIdVoo(rs.getInt("idVoo"));
                    reserva.setDataReserva(rs.getTimestamp("dataReserva").toLocalDateTime());
                    reserva.setNomePassageiro(rs.getString("nomePassageiro"));
                    reserva.setCpfPassageiro(rs.getString("cpfPassageiro"));
                    reserva.setOrigem(rs.getString("origem"));
                    reserva.setChegada(rs.getString("chegada"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public Map<Integer, Integer> contarReservasParaTodosOsVoos() {
        Map<Integer, Integer> mapaContagemReservas = new HashMap<>();
        String sql = "SELECT idVoo, COUNT(*) AS contagem FROM Reserva GROUP BY idVoo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mapaContagemReservas.put(rs.getInt("idVoo"), rs.getInt("contagem"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapaContagemReservas;
    }

    public int contarReservasParaVoo(int idVoo) {
        int contagem = 0;
        String sql = "SELECT COUNT(*) AS contagem FROM Reserva WHERE idVoo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVoo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contagem = rs.getInt("contagem");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contagem;
    }

    public boolean isPassageiroJaReservado(int idPassageiro, int idVoo) {
        String sql = "SELECT COUNT(*) AS contagem FROM Reserva WHERE idPassageiro = ? AND idVoo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPassageiro);
            stmt.setInt(2, idVoo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("contagem") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}