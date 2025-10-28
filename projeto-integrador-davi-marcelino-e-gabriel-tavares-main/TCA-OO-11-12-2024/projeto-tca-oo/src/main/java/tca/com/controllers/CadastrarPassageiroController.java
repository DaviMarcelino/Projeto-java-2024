package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import tca.com.App;
import tca.com.model.Passageiro; 
import tca.com.dao.PassageiroDAO; 
import java.time.LocalDate;

public class CadastrarPassageiroController {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefone;
    @FXML
    private DatePicker dataNascimento;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnVoltar;

    private PassageiroDAO passageiroDAO;

    public CadastrarPassageiroController() {
        this.passageiroDAO = new PassageiroDAO();
    }

    @FXML
    private void btnSalvar(ActionEvent event) {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();
        LocalDate dataNascimentoValue = dataNascimento.getValue();

        if (!isValidCpf(cpf)) {
            showAlert("Erro", "O CPF deve ter exatamente 11 dígitos e conter apenas números.");
            return;
        }

        if (!isValidTelefone(telefone)) {
            showAlert("Erro", "O telefone deve ter exatamente 11 dígitos e conter apenas números.");
            return;
        }

        if (passageiroDAO.buscarPorCpf(cpf) != null) { 
            showAlert("Erro", "Já existe um passageiro cadastrado com este CPF.");
            return;
        }

        Passageiro newPassageiro = new Passageiro(0, nome, cpf, email, telefone, dataNascimentoValue);
        passageiroDAO.salvar(newPassageiro); 

        showAlert("Sucesso", "Passageiro cadastrado com sucesso!");
        App.mudaTela("views/TelaPrincipal.fxml");
    }

    private boolean isValidCpf(String cpf) {
        return cpf != null && cpf.length() == 11 && cpf.matches("\\d+");
    }

    private boolean isValidTelefone(String telefone) {
        return telefone != null && telefone.length() == 11 && telefone.matches("\\d+");
    }

    @FXML
    private void btnVoltar(ActionEvent event) {
        App.mudaTela("views/TelaPrincipal.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}