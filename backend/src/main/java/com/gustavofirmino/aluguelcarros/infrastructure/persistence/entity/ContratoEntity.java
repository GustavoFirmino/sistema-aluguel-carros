package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um contrato de aluguel.
 * Gerado automaticamente quando um pedido é marcado como CONCLUIDO.
 * Pode estar vinculado a um contrato de crédito de um banco agente.
 */
@Serdeable
@Entity
@Table(name = "contratos")
public class ContratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agente_id")
    private AgenteEntity agente;

    @Column(nullable = false, unique = true)
    private String numeroContrato;

    @Column(nullable = false)
    private LocalDateTime dataContrato;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private Long diasAluguel;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiaria;

    @Column(nullable = false)
    private boolean possuiContratoCredito = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "banco_agente_id")
    private AgenteEntity bancoAgente;

    public ContratoEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoEntity getPedido() { return pedido; }
    public void setPedido(PedidoEntity pedido) { this.pedido = pedido; }

    public AgenteEntity getAgente() { return agente; }
    public void setAgente(AgenteEntity agente) { this.agente = agente; }

    public String getNumeroContrato() { return numeroContrato; }
    public void setNumeroContrato(String numeroContrato) { this.numeroContrato = numeroContrato; }

    public LocalDateTime getDataContrato() { return dataContrato; }
    public void setDataContrato(LocalDateTime dataContrato) { this.dataContrato = dataContrato; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Long getDiasAluguel() { return diasAluguel; }
    public void setDiasAluguel(Long diasAluguel) { this.diasAluguel = diasAluguel; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }

    public boolean isPossuiContratoCredito() { return possuiContratoCredito; }
    public void setPossuiContratoCredito(boolean possuiContratoCredito) { this.possuiContratoCredito = possuiContratoCredito; }

    public AgenteEntity getBancoAgente() { return bancoAgente; }
    public void setBancoAgente(AgenteEntity bancoAgente) { this.bancoAgente = bancoAgente; }
}
