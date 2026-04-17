package com.gustavofirmino.aluguelcarros.domain.model;

import java.math.BigDecimal;

public class Automovel {

    private Long id;
    private String matricula;
    private Integer ano;
    private String marca;
    private String modelo;
    private String placa;
    private String cor;
    private boolean disponivel;
    private BigDecimal valorDiaria;

    public Automovel() {}

    public Automovel(Long id, String matricula, Integer ano, String marca, String modelo,
                     String placa, String cor, boolean disponivel, BigDecimal valorDiaria) {
        this.id = id; this.matricula = matricula; this.ano = ano;
        this.marca = marca; this.modelo = modelo; this.placa = placa;
        this.cor = cor; this.disponivel = disponivel; this.valorDiaria = valorDiaria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }
}
