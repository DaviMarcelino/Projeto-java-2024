package tca.com.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import tca.com.App;
import tca.com.dao.ReservaDAO;
import tca.com.model.Reserva;

import java.util.List;

public class ListarReservasController {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnVoltar;
    @FXML
    private TableView<Reserva> tabelaReservas;
    @FXML
    private TableColumn<Reserva, Integer> clnId;
    @FXML
    private TableColumn<Reserva, String> clnNomePassageiro;
    @FXML
    private TableColumn<Reserva, String> clnCpfPassageiro;
    @FXML
    private TableColumn<Reserva, Integer> clnIdVoo;
    @FXML
    private TableColumn<Reserva, String> clnOrigem;
    @FXML
    private TableColumn<Reserva, String> clnChegada;
    @FXML
    private TableColumn<Reserva, String> clnDataPartida;

    private ReservaDAO reservaDAO;
    private ObservableList<Reserva> reservasList;

    @FXML
    private void initialize() {
        reservaDAO = new ReservaDAO();
        reservasList = FXCollections.observableArrayList();
        clnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clnNomePassageiro.setCellValueFactory(new PropertyValueFactory<>("nomePassageiro"));
        clnCpfPassageiro.setCellValueFactory(new PropertyValueFactory<>("cpfPassageiro"));
        clnIdVoo.setCellValueFactory(new PropertyValueFactory<>("idVoo"));
        clnOrigem.setCellValueFactory(new PropertyValueFactory<>("origem"));
        clnChegada.setCellValueFactory(new PropertyValueFactory<>("chegada"));
        clnDataPartida.setCellValueFactory(new PropertyValueFactory<>("formattedDataReserva"));
        carregarReservas();
    }

    private void carregarReservas() {
        List<Reserva> reservas = reservaDAO.buscarTodas();
        reservasList.setAll(reservas);
        tabelaReservas.setItems(reservasList);
    }

    @FXML
    private void buscar() {
        String termoBusca = txtBuscar.getText();
        List<Reserva> reservasFiltradas = reservaDAO.buscarReservas(termoBusca);
        reservasList.setAll(reservasFiltradas);
    }

    @FXML
    private void cancelar() {
        Reserva reservaSelecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (reservaSelecionada != null) {
            Alert alertaConfirmacao = new Alert(AlertType.CONFIRMATION);
            alertaConfirmacao.setTitle("Confirmação de Cancelamento");
            alertaConfirmacao.setContentText("Deseja cancelar essa reserva?");
            alertaConfirmacao.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.OK) {
                    reservaDAO.remover(reservaSelecionada.getId());
                    carregarReservas();
                }
            });
        } else {
            showAlert("Erro", "Nenhuma reserva selecionada.");
        }
    }

    @FXML
    private void voltar() {
        App.mudaTela("./views/TelaPrincipal.fxml");
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}