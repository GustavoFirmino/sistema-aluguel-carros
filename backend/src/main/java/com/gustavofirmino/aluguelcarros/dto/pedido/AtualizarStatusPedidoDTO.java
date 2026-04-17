package com.gustavofirmino.aluguelcarros.dto.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class AtualizarStatusPedidoDTO {

    @NotNull(message = "Status é obrigatório.")
    private StatusPedido status;

    private String observacao;

    private Long agenteId;

    public AtualizarStatusPedidoDTO() {}

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Long getAgenteId() { return agenteId; }
    public void setAgenteId(Long agenteId) { this.agenteId = agenteId; }
}
