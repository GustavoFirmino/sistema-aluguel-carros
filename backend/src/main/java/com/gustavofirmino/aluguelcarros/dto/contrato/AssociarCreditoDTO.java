package com.gustavofirmino.aluguelcarros.dto.contrato;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para associar um contrato de crédito bancário a um contrato de aluguel existente.
 * Conforme especificação: o aluguel pode estar associado a crédito concedido por um banco agente.
 */
@Serdeable
public class AssociarCreditoDTO {

    @NotNull(message = "ID do banco agente é obrigatório.")
    private Long bancoAgenteId;

    public AssociarCreditoDTO() {}

    public Long getBancoAgenteId() { return bancoAgenteId; }
    public void setBancoAgenteId(Long bancoAgenteId) { this.bancoAgenteId = bancoAgenteId; }
}
