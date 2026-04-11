package com.gustavofirmino.aluguelcarros.dto.cliente;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO para uma entidade empregadora vinculada ao cliente.
 * Conforme especificação: clientes podem ter até 3 empregadores.
 */
@Serdeable
public class EmpregadorDTO {

    @NotBlank(message = "Nome da empresa é obrigatório.")
    private String nomeEmpresa;

    @NotNull(message = "Rendimento é obrigatório.")
    @DecimalMin(value = "0.01", message = "Rendimento deve ser maior que zero.")
    private BigDecimal rendimento;

    public EmpregadorDTO() {}

    public EmpregadorDTO(String nomeEmpresa, BigDecimal rendimento) {
        this.nomeEmpresa = nomeEmpresa;
        this.rendimento = rendimento;
    }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public BigDecimal getRendimento() { return rendimento; }
    public void setRendimento(BigDecimal rendimento) { this.rendimento = rendimento; }
}
