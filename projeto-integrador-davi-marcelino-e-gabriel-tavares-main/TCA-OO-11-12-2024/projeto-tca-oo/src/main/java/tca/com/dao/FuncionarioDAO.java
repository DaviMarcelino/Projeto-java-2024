package tca.com.dao;

import tca.com.BD.DatabaseConnection;
import tca.com.model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO {

    public void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS Funcionario (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "nome VARCHAR(100), " +
                     "cpf VARCHAR(11) UNIQUE, " +
                     "email VARCHAR(100), " +
                     "telefone VARCHAR(15), " +
                     "nascimento DATE, " +
                     "senha VARCHAR(100))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Funcionario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Funcionario WHERE cpf = ?";
        Funcionario funcionario = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                funcionario = new Funcionario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("nascimento"),
                    rs.getString("senha") 
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }
}