package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidade JPA que representa um automóvel na base de dados.
 * Campos conforme especificação: matrícula, ano, marca, modelo e placa.
 * valorDiaria é usado para cálculo automático do valor total do contrato.
 */
@Serdeable
@Entity
@Table(name = "automoveis")
public class AutomovelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private boolean disponivel = true;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiaria;

    public AutomovelEntity() {}

    public AutomovelEntity(Long id, String matricula, Integer ano, String marca, String modelo,
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

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }
}
