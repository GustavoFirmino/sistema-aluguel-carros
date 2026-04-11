package com.gustavofirmino.aluguelcarros.dto.automovel;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada para criação e atualização de automóvel.
 */
@Serdeable
public class AutomovelRequestDTO {

    @NotBlank(message = "Matrícula é obrigatória.")
    private String matricula;

    @NotNull(message = "Ano é obrigatório.")
    @Min(value = 1900, message = "Ano deve ser maior que 1900.")
    private Integer ano;

    @NotBlank(message = "Marca é obrigatória.")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório.")
    private String modelo;

    @NotBlank(message = "Placa é obrigatória.")
    private String placa;

    @NotNull(message = "Valor da diária é obrigatório.")
    @DecimalMin(value = "0.01", message = "Valor da diária deve ser maior que zero.")
    private BigDecimal valorDiaria;

    public AutomovelRequestDTO() {}

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }
}
