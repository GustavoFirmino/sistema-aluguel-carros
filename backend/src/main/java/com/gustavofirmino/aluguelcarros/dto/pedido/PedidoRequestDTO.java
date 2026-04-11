package com.gustavofirmino.aluguelcarros.dto.pedido;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO de entrada para criação de um pedido de aluguel.
 * O cliente informa qual automóvel deseja e o período desejado.
 */
@Serdeable
public class PedidoRequestDTO {

    @NotNull(message = "ID do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "ID do automóvel é obrigatório.")
    private Long automovelId;

    @NotNull(message = "Data de início é obrigatória.")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória.")
    @Future(message = "Data de fim deve ser uma data futura.")
    private LocalDate dataFim;

    private String observacao;

    public PedidoRequestDTO() {
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getAutomovelId() { return automovelId; }
    public void setAutomovelId(Long automovelId) { this.automovelId = automovelId; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
