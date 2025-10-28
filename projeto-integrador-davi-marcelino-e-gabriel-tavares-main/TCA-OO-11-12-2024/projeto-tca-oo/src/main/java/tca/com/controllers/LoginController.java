package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import tca.com.App;
import tca.com.dao.FuncionarioDAO;
import tca.com.model.Funcionario;

public class LoginController {

    @FXML
    private TextField txtCpf; 
    @FXML
    private PasswordField txtSenha; 
    @FXML
    private Button btnLogar;

    private FuncionarioDAO funcionarioDAO;

    public LoginController() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    @FXML
    private void logar(ActionEvent event) { 
        String cpf = txtCpf.getText(); 
        String senha = txtSenha.getText();

        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert("Erro de Login", "Por favor, preencha todos os campos.");
            return;
        }

        if (!isValidCpf(cpf)) {
            showAlert("Erro de Login", "O CPF deve conter exatamente 11 dígitos e ser composto apenas por números.");
            return;
        }

        Funcionario funcionario = funcionarioDAO.buscarPorCpf(cpf); 

        if (funcionario != null && senha.equals(funcionario.getSenha())) {
            App.mudaTela("views/TelaPrincipal.fxml");
        } else {
            showAlert("Erro de Login", "CPF ou senha inválidos.");
        }
    }

    private boolean isValidCpf(String cpf) {
        return cpf != null && cpf.length() == 11 && cpf.matches("\\d+");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}