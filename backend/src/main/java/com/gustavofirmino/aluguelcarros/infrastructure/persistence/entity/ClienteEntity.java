package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA que representa um cliente na base de dados.
 * Armazena dados de identificação e até 3 entidades empregadoras
 * com seus respectivos rendimentos, conforme especificação do sistema.
 */
@Serdeable
@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String profissao;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_empregadores", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<EmpregadorEmbeddable> empregadores = new ArrayList<>();

    @Column(precision = 10, scale = 2)
    private BigDecimal rendaMensal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "banco_agente_id")
    private AgenteEntity bancoAgente;

    public ClienteEntity() {}

    public ClienteEntity(Long id, String nome, String cpf, String rg, String endereco, String profissao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.profissao = profissao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public List<EmpregadorEmbeddable> getEmpregadores() { return empregadores; }
    public void setEmpregadores(List<EmpregadorEmbeddable> empregadores) { this.empregadores = empregadores; }

    public BigDecimal getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(BigDecimal rendaMensal) { this.rendaMensal = rendaMensal; }

    public AgenteEntity getBancoAgente() { return bancoAgente; }
    public void setBancoAgente(AgenteEntity bancoAgente) { this.bancoAgente = bancoAgente; }
}
