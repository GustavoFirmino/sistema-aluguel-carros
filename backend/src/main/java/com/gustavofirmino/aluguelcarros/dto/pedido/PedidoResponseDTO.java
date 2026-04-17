package com.gustavofirmino.aluguelcarros.dto.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Serdeable
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private String clienteCpf;
    private Long automovelId;
    private String automovelMarca;
    private String automovelModelo;
    private String automovelPlaca;
    private BigDecimal valorDiaria;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;
    private Long bancoId;
    private String bancoNome;
    private Long empresaId;
    private String empresaNome;

    public PedidoResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public Long getAutomovelId() { return automovelId; }
    public void setAutomovelId(Long automovelId) { this.automovelId = automovelId; }
    public String getAutomovelMarca() { return automovelMarca; }
    public void setAutomovelMarca(String automovelMarca) { this.automovelMarca = automovelMarca; }
    public String getAutomovelModelo() { return automovelModelo; }
    public void setAutomovelModelo(String automovelModelo) { this.automovelModelo = automovelModelo; }
    public String getAutomovelPlaca() { return automovelPlaca; }
    public void setAutomovelPlaca(String automovelPlaca) { this.automovelPlaca = automovelPlaca; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }
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
    public Long getBancoId() { return bancoId; }
    public void setBancoId(Long bancoId) { this.bancoId = bancoId; }
    public String getBancoNome() { return bancoNome; }
    public void setBancoNome(String bancoNome) { this.bancoNome = bancoNome; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }
}
