package com.gustavofirmino.aluguelcarros.dto.contrato;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Serdeable
public class ContratoResponseDTO {

    private Long id;
    private Long pedidoId;
    private String numeroContrato;
    private String clienteNome;
    private String clienteCpf;
    private String automovelDescricao;
    private String agenteNome;
    private Long diasAluguel;
    private BigDecimal valorDiaria;
    private BigDecimal valorTotal;
    private LocalDateTime dataContrato;
    private boolean possuiContratoCredito;
    private String bancoAgenteNome;

    public ContratoResponseDTO() {}

    public ContratoResponseDTO(Long id, Long pedidoId, String numeroContrato, String clienteNome,
                                String clienteCpf, String automovelDescricao, String agenteNome,
                                Long diasAluguel, BigDecimal valorDiaria, BigDecimal valorTotal,
                                LocalDateTime dataContrato, boolean possuiContratoCredito, String bancoAgenteNome) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.numeroContrato = numeroContrato;
        this.clienteNome = clienteNome;
        this.clienteCpf = clienteCpf;
        this.automovelDescricao = automovelDescricao;
        this.agenteNome = agenteNome;
        this.diasAluguel = diasAluguel;
        this.valorDiaria = valorDiaria;
        this.valorTotal = valorTotal;
        this.dataContrato = dataContrato;
        this.possuiContratoCredito = possuiContratoCredito;
        this.bancoAgenteNome = bancoAgenteNome;
    }

    public Long getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public String getNumeroContrato() { return numeroContrato; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteCpf() { return clienteCpf; }
    public String getAutomovelDescricao() { return automovelDescricao; }
    public String getAgenteNome() { return agenteNome; }
    public Long getDiasAluguel() { return diasAluguel; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getDataContrato() { return dataContrato; }
    public boolean isPossuiContratoCredito() { return possuiContratoCredito; }
    public String getBancoAgenteNome() { return bancoAgenteNome; }
}
