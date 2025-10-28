package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import tca.com.App;
import tca.com.dao.PassageiroDAO;
import tca.com.model.Passageiro;

public class EditarPassageiroController {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefone;
    @FXML
    private DatePicker txtDataNascimento;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnVoltar;

    private PassageiroDAO passageiroDAO;
    private Passageiro passageiro;

    private static String cpf;

    public EditarPassageiroController() {
        passageiroDAO = new PassageiroDAO();
    }

    public static void setCpf(String cpf) {
        EditarPassageiroController.cpf = cpf;
    }

    @FXML
    private void initialize() {
        if (cpf != null) {
            loadPassengerData(cpf);
        } else {
            showAlert("Erro", "CPF não fornecido.");
        }
    }

    private void loadPassengerData(String cpf) {
        passageiro = passageiroDAO.buscarPorCpf(cpf); 
        if (passageiro != null) {
            txtNome.setText(passageiro.getNome());
            txtEmail.setText(passageiro.getEmail());
            txtTelefone.setText(passageiro.getTelefone());
            txtDataNascimento.setValue(passageiro.getDataNascimento());
        } else {
            showAlert("Erro", "Passageiro não encontrado.");
        }
    }

    @FXML
    private void btnSalvar(ActionEvent event) {
        if (passageiro != null) {
            String telefone = txtTelefone.getText();
            if (!isTelefoneValido(telefone)) {
                showAlert("Erro", "O telefone deve ter 11 dígitos e conter apenas números.");
                return;
            }

            passageiro.setNome(txtNome.getText());
            passageiro.setEmail(txtEmail.getText());
            passageiro.setTelefone(telefone);
            passageiro.setDataNascimento(txtDataNascimento.getValue());

            passageiroDAO.atualizar(passageiro); 
            showAlert("Sucesso", "Passageiro atualizado com sucesso!");
            App.mudaTela("views/ListarPassageiros.fxml");
        } else {
            showAlert("Erro", "Não foi possível atualizar o passageiro.");
        }
    }

    @FXML
    private void btnVoltar(ActionEvent event) {
        App.mudaTela("views/ListarPassageiros.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isTelefoneValido(String telefone) {
        return telefone.matches("\\d{11}");
    }
}