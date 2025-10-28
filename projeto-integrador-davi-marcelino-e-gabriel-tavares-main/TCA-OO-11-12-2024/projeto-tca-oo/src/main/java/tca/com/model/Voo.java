package tca.com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Voo {
    private int id;
    private String origem;
    private String chegada;
    private LocalDate dataVoo;
    private LocalDateTime horaPartida;
    private LocalDateTime horaChegada;
    private int capacidadePassageiros;
    private float valorPassagem;
    private String capacidadePassageirosInfo; // Adicionado para armazenar informações de capacidade

    public Voo() {
    }

    public Voo(int id, LocalDate dataVoo, String origem, String chegada, LocalDateTime horaPartida,
               LocalDateTime horaChegada, int capacidade, float valorPassagem) {
        this.id = id;
        this.dataVoo = dataVoo;
        this.origem = origem;
        this.chegada = chegada;
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
        this.capacidadePassageiros = capacidade;
        this.valorPassagem = valorPassagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getChegada() {
        return chegada;
    }

    public void setChegada(String chegada) {
        this.chegada = chegada;
    }

    public LocalDate getDataVoo() {
        return dataVoo;
    }

    public void setDataVoo(LocalDate dataVoo) {
        this.dataVoo = dataVoo;
    }

    public LocalDateTime getHoraPartida() {
        return horaPartida;
    }

    public void setHoraPartida(LocalDateTime horaPartida) {
        this.horaPartida = horaPartida;
    }

    public LocalDateTime getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(LocalDateTime horaChegada) {
        this.horaChegada = horaChegada;
    }

    public int getCapacidadePassageiros() {
        return capacidadePassageiros;
    }

    public void setCapacidadePassageiros(int capacidadePassageiros) {
        this.capacidadePassageiros = capacidadePassageiros;
    }

    public float getValorPassagem() {
        return valorPassagem;
    }

    public void setValorPassagem(float valorPassagem) {
        this.valorPassagem = valorPassagem;
    }

    public String getCapacidadePassageirosInfo() {
        return capacidadePassageirosInfo;
    }

    public void setCapacidadePassageirosInfo(String capacidadePassageirosInfo) {
        this.capacidadePassageirosInfo = capacidadePassageirosInfo;
    }

    @Override
    public String toString() {
        return String.format("%s --> %s - %s - %s - R$%.2f", 
            origem, chegada, horaPartida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 
            capacidadePassageirosInfo, valorPassagem);
    }
}