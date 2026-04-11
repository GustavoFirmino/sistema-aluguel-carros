package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * Tipo embutido que representa uma entidade empregadora de um cliente.
 * Conforme especificação: clientes podem ter até 3 entidades empregadoras
 * com seus respectivos rendimentos auferidos.
 */
@Serdeable
@Embeddable
public class EmpregadorEmbeddable {

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(name = "rendimento", nullable = false)
    private BigDecimal rendimento;

    public EmpregadorEmbeddable() {}

    public EmpregadorEmbeddable(String nomeEmpresa, BigDecimal rendimento) {
        this.nomeEmpresa = nomeEmpresa;
        this.rendimento = rendimento;
    }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public BigDecimal getRendimento() { return rendimento; }
    public void setRendimento(BigDecimal rendimento) { this.rendimento = rendimento; }
}
