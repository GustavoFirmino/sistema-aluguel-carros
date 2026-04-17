package com.gustavofirmino.aluguelcarros.domain.model;

/**
 * Enumeração dos possíveis estados de um pedido de aluguel.
 *
 * Fluxo principal:
 *   PENDENTE → APROVADO_BANCO → CONCLUIDO (contrato fechado pela empresa)
 *   PENDENTE → REJEITADO (pelo banco)
 *   APROVADO_BANCO → REJEITADO (pela empresa)
 *   PENDENTE → CANCELADO (pelo cliente)
 */
public enum StatusPedido {

    /** Pedido criado pelo cliente, aguardando análise do banco. */
    PENDENTE,

    /** Pedido aprovado pelo banco; aguardando empresa fechar contrato. */
    APROVADO_BANCO,

    /** Pedido rejeitado pelo banco ou pela empresa. */
    REJEITADO,

    /** Pedido cancelado pelo próprio cliente. */
    CANCELADO,

    /** Contrato fechado pela empresa — aluguel concluído. */
    CONCLUIDO
}
