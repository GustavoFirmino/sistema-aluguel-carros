package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Serdeable
@Entity
@Table(name = "automoveis")
public class AutomovelEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String matricula;
    @Column(nullable = false) private Integer ano;
    @Column(nullable = false) private String marca;
    @Column(nullable = false) private String modelo;
    @Column(nullable = false, unique = true) private String placa;
    @Column private String cor;
    @Column(nullable = false) private boolean disponivel = true;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal valorDiaria;

    public AutomovelEntity() {}

    public AutomovelEntity(Long id, String matricula, Integer ano, String marca, String modelo,
                           String placa, String cor, boolean disponivel, BigDecimal valorDiaria) {
        this.id = id; this.matricula = matricula; this.ano = ano;
        this.marca = marca; this.modelo = modelo; this.placa = placa;
        this.cor = cor; this.disponivel = disponivel; this.valorDiaria = valorDiaria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String m) { this.matricula = m; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public String getMarca() { return marca; }
    public void setMarca(String m) { this.marca = m; }
    public String getModelo() { return modelo; }
    public void setModelo(String m) { this.modelo = m; }
    public String getPlaca() { return placa; }
    public void setPlaca(String p) { this.placa = p; }
    public String getCor() { return cor; }
    public void setCor(String c) { this.cor = c; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean d) { this.disponivel = d; }
    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal v) { this.valorDiaria = v; }
}
