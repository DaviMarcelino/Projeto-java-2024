package tca.com.dao;

import tca.com.BD.DatabaseConnection;
import tca.com.model.Passageiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PassageiroDAO {

    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS Passageiro (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nome VARCHAR(100), " +
                "cpf VARCHAR(11) UNIQUE, " +
                "email VARCHAR(100), " +
                "telefone VARCHAR(15), " +
                "nascimento DATE)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Passageiro passageiro) {
        String sql = "INSERT INTO Passageiro (nome, cpf, email, telefone, nascimento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passageiro.getNome());
            stmt.setString(2, passageiro.getCpf());
            stmt.setString(3, passageiro.getEmail());
            stmt.setString(4, passageiro.getTelefone());
            stmt.setDate(5, java.sql.Date.valueOf(passageiro.getDataNascimento()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Passageiro buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Passageiro WHERE cpf = ?";
        Passageiro passageiro = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                passageiro = new Passageiro(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getDate("nascimento").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passageiro;
    }

    public void atualizar(Passageiro passageiro) {
        String sql = "UPDATE Passageiro SET nome = ?, email = ?, telefone = ?, nascimento = ? WHERE cpf = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passageiro.getNome());
            stmt.setString(2, passageiro.getEmail());
            stmt.setString(3, passageiro.getTelefone());
            stmt.setDate(4, java.sql.Date.valueOf(passageiro.getDataNascimento()));
            stmt.setString(5, passageiro.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(String cpf) {
        String sql = "DELETE FROM Passageiro WHERE cpf = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Passageiro> listarPassageiros() {
        List<Passageiro> passageirosLista = new ArrayList<>();
        String sql = "SELECT * FROM Passageiro";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Passageiro passageiro = new Passageiro(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getDate("nascimento").toLocalDate());
                passageirosLista.add(passageiro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passageirosLista;
    }
}