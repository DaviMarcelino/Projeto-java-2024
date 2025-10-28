package tca.com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import tca.com.dao.PassageiroDAO;
import tca.com.model.Passageiro;
import tca.com.App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.scene.control.ButtonType;
import javafx.util.converter.LocalDateStringConverter;

public class ListaPassageirosController {

    @FXML
    private TableView<Passageiro> tabelaPassageiros;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnEditar; 
    @FXML
    private Button btnExcluir; 

    @FXML
    private TableColumn<Passageiro, String> clnNome;
    @FXML
    private TableColumn<Passageiro, String> clnCpf;
    @FXML
    private TableColumn<Passageiro, String> clnEmail;
    @FXML
    private TableColumn<Passageiro, String> clnTelefone;
    @FXML
    private TableColumn<Passageiro, LocalDate> clnDataNascimento;

    private PassageiroDAO passageiroDAO;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        passageiroDAO = new PassageiroDAO();
        clnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        clnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        clnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        clnDataNascimento.setCellFactory(column -> new TextFieldTableCell<>(new LocalDateStringConverter()));
        carregarPassageiros();
    }

    private void carregarPassageiros() {
        try {
            List<Passageiro> passageiros = passageiroDAO.listarPassageiros();
            if (passageiros.isEmpty()) {
                showAlert("Nenhum Passageiro Encontrado", "Não há passageiros cadastrados.");
            }
            tabelaPassageiros.getItems().clear();
            tabelaPassageiros.getItems().addAll(passageiros);
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro ao carregar os passageiros: " + e.getMessage());
        }
    }

    @FXML
    private void buscar() {
        String textoBusca = txtBuscar.getText().toLowerCase();
        List<Passageiro> passageiros = passageiroDAO.listarPassageiros();
        List<Passageiro> passageirosFiltrados = passageiros.stream()
                .filter(p -> p.getNome().toLowerCase().contains(textoBusca) || p.getCpf().contains(textoBusca))
                .collect(Collectors.toList());
        tabelaPassageiros.getItems().clear();
        tabelaPassageiros.getItems().addAll(passageirosFiltrados);
    }

    @FXML
    private void voltar() {
        App.mudaTela("views/TelaPrincipal.fxml");
    }

    @FXML
    private void editar() {
        Passageiro passageiroSelecionado = tabelaPassageiros.getSelectionModel().getSelectedItem();
        if (passageiroSelecionado != null) {
            String cpfDoPassageiro = passageiroSelecionado.getCpf();
            EditarPassageiroController.setCpf(cpfDoPassageiro);
            App.mudaTela("views/EditarPassageiro.fxml");
        } else {
            showAlert("Nenhum Passageiro Selecionado", "Por favor, selecione um passageiro para editar.");
        }
    }

    @FXML
    private void excluir() {
        Passageiro passageiroSelecionado = tabelaPassageiros.getSelectionModel().getSelectedItem();
        if (passageiroSelecionado != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmação de Exclusão");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Tem certeza que deseja excluir o passageiro: " + passageiroSelecionado.getNome() + "?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                passageiroDAO.remover(passageiroSelecionado.getCpf()); 
                carregarPassageiros();
                showAlert("Passageiro Excluído", "Passageiro excluído com sucesso.");
            }
        } else {
            showAlert("Nenhum Passageiro Selecionado", "Por favor, selecione um passageiro para excluir.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}