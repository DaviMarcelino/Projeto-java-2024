package tca.com.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tca.com.dao.VooDAO;
import tca.com.dao.ReservaDAO;
import tca.com.model.Voo;
import tca.com.App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListarVoosController {

    @FXML
    private TableView<Voo> tabelaVoos;
    @FXML
    private TableColumn<Voo, Integer> clnCodigoVoo;
    @FXML
    private TableColumn<Voo, String> clnLocalPartida;
    @FXML
    private TableColumn<Voo, String> clnLocalChegada;
    @FXML
    private TableColumn<Voo, String> clnDataVoo;
    @FXML
    private TableColumn<Voo, String> clnHoraPartida;
    @FXML
    private TableColumn<Voo, String> clnHoraChegada;
    @FXML
    private TableColumn<Voo, String> clnCapacidade;
    @FXML
    private TableColumn<Voo, Float> clnValorPassagem;
    @FXML
    private Button btnVoltar;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    private VooDAO vooDAO;
    private ReservaDAO reservaDAO;
    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        vooDAO = new VooDAO();
        reservaDAO = new ReservaDAO();

        clnCodigoVoo.setCellValueFactory(new PropertyValueFactory<>("id"));
        clnLocalPartida.setCellValueFactory(new PropertyValueFactory<>("origem"));
        clnLocalChegada.setCellValueFactory(new PropertyValueFactory<>("chegada"));

        clnDataVoo.setCellValueFactory(cellData -> {
            LocalDate dataVoo = cellData.getValue().getDataVoo();
            String dataFormatada = dataVoo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new javafx.beans.property.SimpleStringProperty(dataFormatada);
        });

        clnHoraPartida.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraPartida().format(formatoData)));
        clnHoraChegada.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoraChegada().format(formatoData)));
        clnCapacidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCapacidadePassageirosInfo()));
        clnValorPassagem.setCellValueFactory(new PropertyValueFactory<>("valorPassagem"));

        carregarVoos();
    }

    private void carregarVoos() {
        Task<List<Voo>> tarefa = new Task<List<Voo>>() {
            @Override
            protected List<Voo> call() throws Exception {
                List<Voo> voos = vooDAO.buscarTodos(); 
                Map<Integer, Integer> mapaContagemReservas = reservaDAO.contarReservasParaTodosOsVoos();
                for (Voo voo : voos) {
                    int contagemReservas = mapaContagemReservas.getOrDefault(voo.getId(), 0);
                    String infoCapacidade = contagemReservas + "/" + voo.getCapacidadePassageiros();
                    voo.setCapacidadePassageirosInfo(infoCapacidade);
                }
                return voos;
            }
        };

        tarefa.setOnSucceeded(event -> {
            tabelaVoos.getItems().setAll(tarefa.getValue());
        });

        tarefa.setOnFailed(event -> {
            showAlert("Erro", "Falha ao carregar voos.");
        });

        new Thread(tarefa).start();
    }

    @FXML
    private void voltar() {
        App.mudaTela("views/TelaPrincipal.fxml");
    }

    @FXML
    private void buscar() {
        String termoBusca = txtBuscar.getText();
        Task<List<Voo>> tarefa = new Task<List<Voo>>() {
            @Override
            protected List<Voo> call() throws Exception {
                List<Voo> voosFiltrados = vooDAO.buscarVoos(termoBusca); 
                Map<Integer, Integer> mapaContagemReservas = reservaDAO.contarReservasParaTodosOsVoos();
                for (Voo voo : voosFiltrados) {
                    int contagemReservas = mapaContagemReservas.getOrDefault(voo.getId(), 0);
                    String infoCapacidade = contagemReservas + "/" + voo.getCapacidadePassageiros();
                    voo.setCapacidadePassageirosInfo(infoCapacidade);
                }
                return voosFiltrados;
            }
        };

        tarefa.setOnSucceeded(event -> {
            tabelaVoos.getItems().setAll(tarefa.getValue());
            if (tarefa.getValue().isEmpty()) {
                showAlert("Resultados da Pesquisa", "Nenhum voo encontrado para o termo de pesquisa: " + termoBusca);
            }
        });

        tarefa.setOnFailed(event -> {
            showAlert("Erro", "Falha ao pesquisar voos.");
        });

        new Thread(tarefa).start();
    }

    @FXML
    private void editar() {
        Voo vooSelecionado = tabelaVoos.getSelectionModel().getSelectedItem();
        if (vooSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tca/com/views/EditarVoo.fxml"));
                Parent editarVooView = loader.load();

                EditarVooController controller = loader.getController();
                controller.setVoo(vooSelecionado); 

                Stage stage = (Stage) btnEditar.getScene().getWindow();
                stage.getScene().setRoot(editarVooView);
            } catch (Exception e) {
                showAlert("Erro", "Não foi possível abrir a tela de edição.");
            }
        } else {
            showAlert("Aviso", "Selecione um voo para editar.");
        }
    }

    @FXML
    private void excluir() {
        Voo vooSelecionado = tabelaVoos.getSelectionModel().getSelectedItem();
        if (vooSelecionado != null) {
            Alert alertaConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            alertaConfirmacao.setTitle("Confirmação de Exclusão");
            alertaConfirmacao.setHeaderText("Você tem certeza que deseja excluir este voo?");
            alertaConfirmacao.setContentText("Voo: " + vooSelecionado.getOrigem() + " para " + vooSelecionado.getChegada());

            Optional<ButtonType> resultado = alertaConfirmacao.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                vooDAO.remover(vooSelecionado.getId()); 
                carregarVoos();
            }
        } else {
            showAlert("Aviso", "Selecione um voo para excluir.");
        }
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}