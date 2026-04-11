package com.gustavofirmino.aluguelcarros.dto.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de saída com os dados completos de um pedido de aluguel.
 * Inclui informações resumidas do cliente e do automóvel.
 */
@Serdeable
public class PedidoResponseDTO {

    private Long id;

    // Dados resumidos do cliente
    private Long clienteId;
    private String clienteNome;
    private String clienteCpf;

    // Dados resumidos do automóvel
    private Long automovelId;
    private String automovelMarca;
    private String automovelModelo;
    private String automovelPlaca;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long id, Long clienteId, String clienteNome, String clienteCpf,
                              Long automovelId, String automovelMarca, String automovelModelo, String automovelPlaca,
                              LocalDate dataInicio, LocalDate dataFim, StatusPedido status,
                              LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, String observacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteCpf = clienteCpf;
        this.automovelId = automovelId;
        this.automovelMarca = automovelMarca;
        this.automovelModelo = automovelModelo;
        this.automovelPlaca = automovelPlaca;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.observacao = observacao;
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteCpf() { return clienteCpf; }
    public Long getAutomovelId() { return automovelId; }
    public String getAutomovelMarca() { return automovelMarca; }
    public String getAutomovelModelo() { return automovelModelo; }
    public String getAutomovelPlaca() { return automovelPlaca; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public StatusPedido getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public String getObservacao() { return observacao; }
}
