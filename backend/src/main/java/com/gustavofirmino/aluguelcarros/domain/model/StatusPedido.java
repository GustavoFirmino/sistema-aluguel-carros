package com.gustavofirmino.aluguelcarros.domain.model;

/**
 * Enumeração dos possíveis estados de um pedido de aluguel.
 *
 * Fluxo principal:
 *   PENDENTE → EM_ANALISE → APROVADO → CONCLUIDO
 *                         ↘ REJEITADO
 *   PENDENTE → CANCELADO (cancelado pelo cliente)
 */
public enum StatusPedido {

    /** Pedido criado pelo cliente, aguardando análise do agente. */
    PENDENTE,

    /** Pedido em análise financeira pelo agente. */
    EM_ANALISE,

    /** Pedido aprovado pelo agente, contrato pode ser emitido. */
    APROVADO,

    /** Pedido rejeitado pelo agente após análise financeira. */
    REJEITADO,

    /** Pedido cancelado pelo próprio cliente antes da aprovação. */
    CANCELADO,

    /** Contrato de aluguel concluído com sucesso. */
    CONCLUIDO
}
