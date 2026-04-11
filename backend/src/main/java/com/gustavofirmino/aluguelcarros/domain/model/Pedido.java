package com.gustavofirmino.aluguelcarros.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de domínio que representa um pedido de aluguel de automóvel.
 * Um pedido associa um cliente a um automóvel por um período determinado.
 */
public class Pedido {

    private Long id;
    private Cliente cliente;
    private Automovel automovel;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;

    public Pedido() {
    }

    public Pedido(Long id, Cliente cliente, Automovel automovel, LocalDate dataInicio, LocalDate dataFim,
                  StatusPedido status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, String observacao) {
        this.id = id;
        this.cliente = cliente;
        this.automovel = automovel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.observacao = observacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Automovel getAutomovel() { return automovel; }
    public void setAutomovel(Automovel automovel) { this.automovel = automovel; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
