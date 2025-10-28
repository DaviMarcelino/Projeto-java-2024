package tca.com.dao;

import tca.com.BD.DatabaseConnection;
import tca.com.model.Voo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VooDAO {

    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS Voo (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "localPartida VARCHAR(100), " +
                "localChegada VARCHAR(100), " +
                "dataVoo DATE, " +
                "horaPartida DATETIME, " +
                "horaChegada DATETIME, " +
                "capacidadePassageiros INT, " +
                "valorPassagem FLOAT)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Voo voo) {
        String sql = "INSERT INTO Voo (localPartida, localChegada, dataVoo, horaPartida, horaChegada, capacidadePassageiros, valorPassagem) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, voo.getOrigem());
            stmt.setString(2, voo.getChegada());
            stmt.setDate(3, java.sql.Date.valueOf(voo.getDataVoo()));
            stmt.setObject(4, voo.getHoraPartida());
            stmt.setObject(5, voo.getHoraChegada());
            stmt.setInt(6, voo.getCapacidadePassageiros());
            stmt.setFloat(7, voo.getValorPassagem());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Voo voo) {
        String sql = "UPDATE Voo SET localPartida = ?, localChegada = ?, dataVoo = ?, horaPartida = ?, horaChegada = ?, capacidadePassageiros = ?, valorPassagem = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, voo.getOrigem());
            stmt.setString(2, voo.getChegada());
            stmt.setDate(3, java.sql.Date.valueOf(voo.getDataVoo()));
            stmt.setObject(4, voo.getHoraPartida());
            stmt.setObject(5, voo.getHoraChegada());
            stmt.setInt(6, voo.getCapacidadePassageiros());
            stmt.setFloat(7, voo.getValorPassagem());
            stmt.setInt(8, voo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM Voo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Voo> buscarTodos() {
        List<Voo> voos = new ArrayList<>();
        String sql = "SELECT * FROM Voo"; 
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Voo voo = new Voo();
                voo.setId(rs.getInt("id"));
                voo.setOrigem(rs.getString("localPartida"));
                voo.setChegada(rs.getString("localChegada"));
                voo.setDataVoo(rs.getDate("dataVoo").toLocalDate());
                voo.setHoraPartida(rs.getTimestamp("horaPartida").toLocalDateTime());
                voo.setHoraChegada(rs.getTimestamp("horaChegada").toLocalDateTime());
                voo.setCapacidadePassageiros(rs.getInt("capacidadePassageiros"));
                voo.setValorPassagem(rs.getFloat("valorPassagem"));
                voos.add(voo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voos;
    }

    public List<Voo> buscarVoos(String termoBusca) {
        List<Voo> voos = new ArrayList<>();
        String sql = "SELECT * FROM Voo WHERE localPartida LIKE ? OR localChegada LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termoBusca + "%");
            stmt.setString(2, "%" + termoBusca + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Voo voo = new Voo();
                    voo.setId(rs.getInt("id"));
                    voo.setOrigem(rs.getString("localPartida"));
                    voo.setChegada(rs.getString("localChegada"));
                    voo.setDataVoo(rs.getDate("dataVoo").toLocalDate());
                    voo.setHoraPartida(rs.getTimestamp("horaPartida").toLocalDateTime());
                    voo.setHoraChegada(rs.getTimestamp("horaChegada").toLocalDateTime());
                    voo.setCapacidadePassageiros(rs.getInt("capacidadePassageiros"));
                    voo.setValorPassagem(rs.getFloat("valorPassagem"));
                    voos.add(voo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voos;
    }

    public Voo buscarPorId(int id) {
        Voo voo = null;
        String sql = "SELECT * FROM Voo WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    voo = new Voo();
                    voo.setId(rs.getInt("id"));
                    voo.setOrigem(rs.getString("localPartida"));
                    voo.setChegada(rs.getString("localChegada"));
                    voo.setDataVoo(rs.getDate("dataVoo").toLocalDate());
                    voo.setHoraPartida(rs.getTimestamp("horaPartida").toLocalDateTime());
                    voo.setHoraChegada(rs.getTimestamp("horaChegada").toLocalDateTime());
                    voo.setCapacidadePassageiros(rs.getInt("capacidadePassageiros"));
                    voo.setValorPassagem(rs.getFloat("valorPassagem"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voo;
    }
}