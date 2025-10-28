package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import tca.com.App;

public class TelaPrincipalController {

    @FXML
    private Button btnCadastrarPassageiro;
    @FXML
    private Button btnCadastrarVoo;
    @FXML
    private Button btnReservarVoo;
    @FXML
    private Button btnRelatorioVoo;
    @FXML
    private Button btnListarVoo;
    @FXML
    private Button btnListarPassageiro;
    @FXML
    private Button btnListarReservas;

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void CadastrarPassageiro(ActionEvent event) {
        App.mudaTela("./views/CadastrarPassageiro.fxml");
    }

    @FXML
    private void CadastrarVoo(ActionEvent event) {
        App.mudaTela("./views/CadastrarVoo.fxml");
    }

    @FXML
    private void ReservarVoo(ActionEvent event) {
        App.mudaTela("./views/ReservarVoo.fxml");
    }

    @FXML
    private void ListarVoo(ActionEvent event) {
        App.mudaTela("./views/ListarVoos.fxml");
    }

    @FXML
    private void ListarPassageiro(ActionEvent event) {
        App.mudaTela("./views/ListarPassageiros.fxml");
    }

    @FXML
    private void TrocarConta(ActionEvent event) {
        App.mudaTela("./views/TelaLogin.fxml");
    }

    @FXML
    private void ListarReservas(ActionEvent event) {
        App.mudaTela("./views/ListarReservas.fxml");
    }
}