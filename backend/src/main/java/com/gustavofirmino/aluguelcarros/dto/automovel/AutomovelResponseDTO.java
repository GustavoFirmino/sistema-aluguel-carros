package com.gustavofirmino.aluguelcarros.dto.automovel;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class AutomovelResponseDTO {

    private Long id;
    private String matricula;
    private Integer ano;
    private String marca;
    private String modelo;
    private String placa;
    private String cor;
    private boolean disponivel;
    private BigDecimal valorDiaria;
    private String fotoUrl;

    public AutomovelResponseDTO() {}

    public AutomovelResponseDTO(Long id, String matricula, Integer ano, String marca, String modelo,
                                String placa, String cor, boolean disponivel, BigDecimal valorDiaria) {
        this.id = id; this.matricula = matricula; this.ano = ano;
        this.marca = marca; this.modelo = modelo; this.placa = placa;
        this.cor = cor; this.disponivel = disponivel; this.valorDiaria = valorDiaria;
        this.fotoUrl = "/automoveis/foto/" + (placa != null ? placa.replace("-", "") : "");
    }

    public Long getId() { return id; }
    public String getMatricula() { return matricula; }
    public Integer getAno() { return ano; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public String getCor() { return cor; }
    public boolean isDisponivel() { return disponivel; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public String getFotoUrl() { return fotoUrl; }
}
