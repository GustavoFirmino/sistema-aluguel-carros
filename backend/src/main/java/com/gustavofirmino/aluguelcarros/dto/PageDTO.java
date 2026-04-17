package com.gustavofirmino.aluguelcarros.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

/**
 * Wrapper genérico para respostas paginadas.
 *
 * Exemplo de resposta JSON:
 * {
 *   "items":      [...],
 *   "total":      42,
 *   "pagina":     0,
 *   "tamanho":    20,
 *   "totalPaginas": 3
 * }
 */
@Serdeable
public record PageDTO<T>(
        // ALWAYS garante que a lista é serializada mesmo quando vazia (comportamento padrão
        // do Micronaut Serde omite coleções vazias em tipos genéricos)
        @JsonInclude(JsonInclude.Include.ALWAYS) List<T> items,
        long total,
        int pagina,
        int tamanho,
        int totalPaginas
) {
    public static <T> PageDTO<T> of(List<T> items, long total, int pagina, int tamanho) {
        int totalPaginas = tamanho == 0 ? 0 : (int) Math.ceil((double) total / tamanho);
        return new PageDTO<>(items, total, pagina, tamanho, totalPaginas);
    }
}
