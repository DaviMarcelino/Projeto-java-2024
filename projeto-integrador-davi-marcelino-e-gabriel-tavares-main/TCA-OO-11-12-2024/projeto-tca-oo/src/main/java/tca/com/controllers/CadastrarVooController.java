package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;
import tca.com.App;
import tca.com.model.Voo;
import tca.com.dao.VooDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastrarVooController {

    @FXML
    private DatePicker txtDataVoo;
    @FXML
    private TextField txtOrigem;
    @FXML
    private TextField txtChegada;
    @FXML
    private TextField txtHoraPartida;
    @FXML
    private TextField txtHoraChegada;
    @FXML
    private TextField txtCapacidade;
    @FXML
    private TextField txtValorPassagem;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnVoltar;

    @FXML
    private void btnSalvar(ActionEvent event) {
        LocalDate dataVoo = txtDataVoo.getValue();
        String origem = txtOrigem.getText();
        String chegada = txtChegada.getText();

        LocalDateTime horaPartida = null;
        LocalDateTime horaChegada = null;

        if (dataVoo == null) {
            showAlert("Erro", "Por favor, selecione a data do voo.");
            return;
        }

        String horaPartidaTexto = txtHoraPartida.getText();
        if (!isHoraValida(horaPartidaTexto)) {
            showAlert("Erro", "Formato de hora de partida inválido. Use o formato 'HH:mm'.");
            return;
        }

        try {
            String[] horaPartidaSplit = horaPartidaTexto.split(":");
            int hora = Integer.parseInt(horaPartidaSplit[0]);
            int minuto = Integer.parseInt(horaPartidaSplit[1]);
            horaPartida = LocalDateTime.of(dataVoo, LocalDateTime.now().withHour(hora).withMinute(minuto).withSecond(0).toLocalTime());
        } catch (DateTimeParseException e) {
            showAlert("Erro", "Formato de hora de partida inválido. Use o formato 'HH:mm'.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            horaChegada = LocalDateTime.parse(txtHoraChegada.getText(), formatter);
        } catch (DateTimeParseException e) {
            showAlert("Erro", "Formato de hora de chegada inválido. Use o formato 'dd/MM/yyyy HH:mm'.");
            return;
        }

        if (horaChegada.isBefore(horaPartida)) {
            showAlert("Erro", "A hora de chegada não pode ser anterior à hora de partida.");
            return;
        }

        int capacidade = Integer.parseInt(txtCapacidade.getText());
        float valorPassagem = Float.parseFloat(txtValorPassagem.getText());

        if (origem.isEmpty() || chegada.isEmpty() || txtHoraPartida.getText().isEmpty()
                || txtHoraChegada.getText().isEmpty() || txtCapacidade.getText().isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos.");
            return;
        }

        Voo novoVoo = new Voo(0, dataVoo, origem, chegada, horaPartida, horaChegada, capacidade, valorPassagem);
        VooDAO vooDAO = new VooDAO();
        vooDAO.salvar(novoVoo); 

        showAlert("Sucesso", "Voo registrado com sucesso!");
        App.mudaTela("views/TelaPrincipal.fxml");
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

    private boolean isHoraValida(String hora) {
        if (hora == null || !hora.matches("\\d{2}:\\d{2}")) {
            return false;
        }

        String[] partes = hora.split(":");
        int horaInt = Integer.parseInt(partes[0]);
        int minutoInt = Integer.parseInt(partes[1]);

        return (horaInt >= 0 && horaInt < 24) && (minutoInt >= 0 && minutoInt < 60);
    }
}