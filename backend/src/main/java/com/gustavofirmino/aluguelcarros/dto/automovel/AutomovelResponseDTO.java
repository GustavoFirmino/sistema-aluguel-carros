package com.gustavofirmino.aluguelcarros.dto.automovel;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

/**
 * DTO de saída com os dados do automóvel retornados pela API.
 */
@Serdeable
public class AutomovelResponseDTO {

    private Long id;
    private String matricula;
    private Integer ano;
    private String marca;
    private String modelo;
    private String placa;
    private boolean disponivel;
    private BigDecimal valorDiaria;

    public AutomovelResponseDTO() {}

    public AutomovelResponseDTO(Long id, String matricula, Integer ano, String marca, String modelo,
                                String placa, boolean disponivel, BigDecimal valorDiaria) {
        this.id = id;
        this.matricula = matricula;
        this.ano = ano;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.disponivel = disponivel;
        this.valorDiaria = valorDiaria;
    }

    public Long getId() { return id; }
    public String getMatricula() { return matricula; }
    public Integer getAno() { return ano; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public boolean isDisponivel() { return disponivel; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
}
