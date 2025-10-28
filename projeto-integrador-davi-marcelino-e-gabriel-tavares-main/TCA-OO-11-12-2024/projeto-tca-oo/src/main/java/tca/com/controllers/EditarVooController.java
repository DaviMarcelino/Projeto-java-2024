package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import tca.com.App;
import tca.com.dao.VooDAO;
import tca.com.model.Voo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditarVooController {

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

    private VooDAO vooDAO;
    private Voo voo;

    private DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EditarVooController() {
        vooDAO = new VooDAO();
    }

    @FXML
    public void initialize() {
        System.out.println("Controlador inicializado.");
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
        if (voo != null) {
            txtDataVoo.setValue(voo.getDataVoo());
            txtOrigem.setText(voo.getOrigem());
            txtChegada.setText(voo.getChegada());
            txtHoraPartida.setText(voo.getHoraPartida().format(horaFormatter));
            txtHoraChegada.setText(voo.getHoraChegada().format(dataHoraFormatter));
            txtCapacidade.setText(String.valueOf(voo.getCapacidadePassageiros()));
            txtValorPassagem.setText(String.valueOf(voo.getValorPassagem()));
        }
    }

    @FXML
    private void salvar(ActionEvent event) {
        if (voo != null) {
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
                // Definindo os segundos como 0
                horaPartida = LocalDateTime.of(dataVoo, LocalDateTime.now().withHour(hora).withMinute(minuto).withSecond(0).toLocalTime());
            } catch (DateTimeParseException e) {
                showAlert("Erro", "Formato de hora de partida inválido. Use o formato 'HH:mm'.");
                return;
            }

            try {
                horaChegada = LocalDateTime.parse(txtHoraChegada.getText(), dataHoraFormatter);
            } catch (DateTimeParseException e) {
                showAlert("Erro", "Formato de hora de chegada inválido. Use o formato 'dd/MM/yyyy HH:mm'.");
                return;
            }

            if (horaChegada.isBefore(horaPartida)) {
                showAlert("Erro", "A hora de chegada não pode ser anterior à hora de partida.");
                return;
            }

            int capacidade;
            float valorPassagem;

            try {
                capacidade = Integer.parseInt(txtCapacidade.getText());
                valorPassagem = Float.parseFloat(txtValorPassagem.getText());
            } catch (NumberFormatException e) {
                showAlert("Erro", "Capacidade ou valor da passagem inválidos.");
                return;
            }

            if (origem.isEmpty() || chegada.isEmpty() || txtHoraPartida.getText().isEmpty()
                    || txtHoraChegada.getText().isEmpty() || txtCapacidade.getText().isEmpty()) {
                showAlert("Erro", "Por favor, preencha todos os campos.");
                return;
            }

            voo.setOrigem(origem);
            voo.setChegada(chegada);
            voo.setDataVoo(dataVoo);
            voo.setHoraPartida(horaPartida);
            voo.setHoraChegada(horaChegada);
            voo.setCapacidadePassageiros(capacidade);
            voo.setValorPassagem(valorPassagem);

            vooDAO.atualizar(voo);
            showAlert("Sucesso", "Voo atualizado com sucesso!");
            App.mudaTela("views/ListarVoos.fxml");
        } else {
            showAlert("Erro", "Não foi possível atualizar o voo.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        App.mudaTela("views/ListarVoos.fxml");
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