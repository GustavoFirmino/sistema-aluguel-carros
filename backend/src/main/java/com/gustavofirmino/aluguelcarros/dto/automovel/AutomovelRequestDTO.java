package com.gustavofirmino.aluguelcarros.dto.automovel;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Serdeable
public class AutomovelRequestDTO {

    @NotBlank(message = "Matrícula é obrigatória.")
    @Size(max = 30) private String matricula;

    @NotNull(message = "Ano é obrigatório.")
    @Min(1900) @Max(2030) private Integer ano;

    @NotBlank(message = "Marca é obrigatória.")
    @Size(max = 50) private String marca;

    @NotBlank(message = "Modelo é obrigatório.")
    @Size(max = 80) private String modelo;

    @NotBlank(message = "Placa é obrigatória.")
    @Pattern(regexp = "[A-Z]{3}-?\\d[A-Z0-9]\\d{2}", message = "Formato de placa inválido.")
    private String placa;

    @Size(max = 40) private String cor;

    @NotNull(message = "Valor da diária é obrigatório.")
    @DecimalMin("0.01") private BigDecimal valorDiaria;

    public AutomovelRequestDTO() {}

    public String getMatricula() { return matricula; }
    public void setMatricula(String m) { this.matricula = m; }
    public Integer getAno() { return ano; }
    public void setAno(Integer a) { this.ano = a; }
    public String getMarca() { return marca; }
    public void setMarca(String m) { this.marca = m; }
    public String getModelo() { return modelo; }
    public void setModelo(String m) { this.modelo = m; }
    public String getPlaca() { return placa; }
    public void setPlaca(String p) { this.placa = p; }
    public String getCor() { return cor; }
    public void setCor(String c) { this.cor = c; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal v) { this.valorDiaria = v; }
}
