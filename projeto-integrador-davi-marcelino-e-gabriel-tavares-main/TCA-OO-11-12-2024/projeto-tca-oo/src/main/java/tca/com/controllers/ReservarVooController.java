package tca.com.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tca.com.App;
import tca.com.dao.PassageiroDAO;
import tca.com.dao.VooDAO;
import tca.com.dao.ReservaDAO;
import tca.com.model.Passageiro;
import tca.com.model.Voo;
import tca.com.model.Reserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservarVooController {

    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnReservar;
    @FXML
    private Label labelDataHoraAtual;
    @FXML
    private Label labelCapacidade;
    @FXML
    private Label labelReservasFeitas;
    @FXML
    private ComboBox<String> cmbBoxPassageiro;
    @FXML
    private ComboBox<String> cmbBoxVoo;
    @FXML
    private TextField txtBuscarVoo;
    @FXML
    private TextField txtBuscarPassageiro;

    private VooDAO vooDAO;
    private ReservaDAO reservaDAO;
    private Map<String, Integer> passageiroIdMap = new HashMap<>();
    private Timeline debounceTimer;

    @FXML
    public void initialize() {
        vooDAO = new VooDAO();
        reservaDAO = new ReservaDAO();

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        labelDataHoraAtual.setText(agora.format(formatter));

        carregarPassageiros();
        carregarVoos();
    }

    private void carregarPassageiros() {
        Task<List<Passageiro>> tarefa = new Task<List<Passageiro>>() {
            @Override
            protected List<Passageiro> call() throws Exception {
                PassageiroDAO passageiroDAO = new PassageiroDAO();
                return passageiroDAO.listarPassageiros();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Passageiro> passageirosLista = getValue();
                for (Passageiro p : passageirosLista) {
                    String infoPassageiro = p.getNome() + " - " + p.getCpf();
                    cmbBoxPassageiro.getItems().add(infoPassageiro);
                    passageiroIdMap.put(infoPassageiro, p.getId());
                }
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        new Thread(tarefa).start();
    }

    private void carregarVoos() {
        Task<List<Voo>> tarefa = new Task<List<Voo>>() {
            @Override
            protected List<Voo> call() throws Exception {
                return vooDAO.buscarTodos();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Voo> voosLista = getValue();
                Map<Integer , Integer> mapaContagemReservas = reservaDAO.contarReservasParaTodosOsVoos();
                atualizarComboBoxVoos(voosLista, mapaContagemReservas);
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        new Thread(tarefa).start();
    }

    private void atualizarComboBoxVoos(List<Voo> voosLista, Map<Integer, Integer> mapaContagemReservas) {
        cmbBoxVoo.getItems().clear();
        for (Voo voo : voosLista) {
            int contagemReservas = mapaContagemReservas.getOrDefault(voo.getId(), 0);
            String infoCapacidade = contagemReservas + "/" + voo.getCapacidadePassageiros();
            voo.setCapacidadePassageirosInfo(infoCapacidade);
            cmbBoxVoo.getItems().add(voo.getId() + " - " + voo.toString());
        }
    }

    @FXML
    private void cmbBoxVooAction() {
        String vooSelecionado = cmbBoxVoo.getSelectionModel().getSelectedItem();
        if (vooSelecionado != null) {
            int idVoo = Integer.parseInt(vooSelecionado.split(" - ")[0]);
            Voo voo = vooDAO.buscarPorId(idVoo); 
            
            if (voo != null) {
                int contagemReservas = reservaDAO.contarReservasParaVoo(idVoo);
                labelCapacidade.setText(String.valueOf(voo.getCapacidadePassageiros()));
                labelReservasFeitas.setText(String.valueOf(contagemReservas));
            }
        }
    }

    @FXML
    private void buscarVoos() {
        if (debounceTimer != null) {
            debounceTimer.stop();
        }

        debounceTimer = new Timeline(new KeyFrame(Duration.millis(300), event -> {
            String termoBusca = txtBuscarVoo.getText().toLowerCase();
            Task<List<Voo>> tarefa = new Task<List<Voo>>() {
                @Override
                protected List<Voo> call() throws Exception {
                    return termoBusca.isEmpty() ? vooDAO.buscarTodos() : vooDAO.buscarVoos(termoBusca); // Alterado para 'buscarTodos' e 'buscarVoos'
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    List<Voo> voosLista = getValue();
                    Map<Integer, Integer> mapaContagemReservas = reservaDAO.contarReservasParaTodosOsVoos();
                    atualizarComboBoxVoos(voosLista, mapaContagemReservas);
                }

                @Override
                protected void failed() {
                    super.failed();
                }
            };
            new Thread(tarefa).start();
        }));
        debounceTimer.play();
    }

    @FXML
    private void buscarPassageiros() {
        String termoBusca = txtBuscarPassageiro.getText().toLowerCase();
        PassageiroDAO passageiroDAO = new PassageiroDAO();
        Task<List<Passageiro>> tarefa = new Task<List<Passageiro>>() {
            @Override
            protected List<Passageiro> call() throws Exception {
                return passageiroDAO.listarPassageiros();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Passageiro> passageirosLista = getValue();
                cmbBoxPassageiro.getItems().clear();
                for (Passageiro p : passageirosLista) {
                    if (p.getNome().toLowerCase().contains(termoBusca) || p.getCpf().contains(termoBusca)) {
                        String infoPassageiro = p.getNome() + " - " + p.getCpf();
                        cmbBoxPassageiro.getItems().add(infoPassageiro);
                        passageiroIdMap.put(infoPassageiro, p.getId());
                    }
                }
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };
        new Thread(tarefa).start();
    }

    @FXML
    private void reservar(ActionEvent event) {
        String passageiroSelecionado = cmbBoxPassageiro.getSelectionModel().getSelectedItem();
        if (passageiroSelecionado == null) {
            showAlert("Erro", "Por favor, selecione um passageiro.");
            return;
        }

        Integer idPassageiro = passageiroIdMap.get(passageiroSelecionado);
        if (idPassageiro == null) {
            showAlert("Erro", "Erro ao obter o ID do passageiro.");
            return;
        }

        String vooSelecionado = cmbBoxVoo.getSelectionModel().getSelectedItem();
        if (vooSelecionado == null) {
            showAlert("Erro", "Por favor, selecione um voo.");
            return;
        }

        int idVoo = Integer.parseInt(vooSelecionado.split(" - ")[0]);

        if (reservaDAO.isPassageiroJaReservado(idPassageiro, idVoo)) {
            showAlert("Erro", "Este passageiro já está reservado para este voo.");
            return;
        }

        Voo voo = vooDAO.buscarPorId(idVoo); 
        int contagemReservas = reservaDAO.contarReservasParaVoo(idVoo);
        if (contagemReservas >= voo.getCapacidadePassageiros()) {
            showAlert("Erro", "Capacidade máxima do voo atingida. Não é possível realizar a reserva.");
            return;
        }

        Reserva reserva = new Reserva();
        reserva.setIdPassageiro(idPassageiro);
        reserva.setIdVoo(idVoo);
        reserva.setDataReserva(LocalDateTime.now());

        reservaDAO.salvar(reserva); 

        showAlert("Sucesso", "Reserva realizada com sucesso!");
        App.mudaTela("./views/TelaPrincipal.fxml");
    }

    @FXML
    private void voltar(ActionEvent event) {
        App.mudaTela("./views/TelaPrincipal.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}