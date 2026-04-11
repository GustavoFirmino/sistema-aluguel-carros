package com.gustavofirmino.aluguelcarros.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de domínio que representa um contrato de aluguel.
 * Gerado automaticamente quando um pedido é concluído.
 * Pode estar associado a um contrato de crédito concedido por um banco agente.
 */
public class Contrato {

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

    public Contrato() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getNumeroContrato() { return numeroContrato; }
    public void setNumeroContrato(String numeroContrato) { this.numeroContrato = numeroContrato; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }

    public String getAutomovelDescricao() { return automovelDescricao; }
    public void setAutomovelDescricao(String automovelDescricao) { this.automovelDescricao = automovelDescricao; }

    public String getAgenteNome() { return agenteNome; }
    public void setAgenteNome(String agenteNome) { this.agenteNome = agenteNome; }

    public Long getDiasAluguel() { return diasAluguel; }
    public void setDiasAluguel(Long diasAluguel) { this.diasAluguel = diasAluguel; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataContrato() { return dataContrato; }
    public void setDataContrato(LocalDateTime dataContrato) { this.dataContrato = dataContrato; }

    public boolean isPossuiContratoCredito() { return possuiContratoCredito; }
    public void setPossuiContratoCredito(boolean possuiContratoCredito) { this.possuiContratoCredito = possuiContratoCredito; }

    public String getBancoAgenteNome() { return bancoAgenteNome; }
    public void setBancoAgenteNome(String bancoAgenteNome) { this.bancoAgenteNome = bancoAgenteNome; }
}
