package com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

/**
 * Entidade JPA que representa um agente (empresa ou banco).
 * Agentes analisam e aprovam pedidos de aluguel.
 * Bancos podem conceder crédito vinculado ao contrato.
 */
@Serdeable
@Entity
@Table(name = "agentes")
public class AgenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAgente tipo;

    @Column
    private String email;

    @Column
    private String telefone;

    public AgenteEntity() {}

    public AgenteEntity(Long id, String nome, String cnpj, TipoAgente tipo, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public TipoAgente getTipo() { return tipo; }
    public void setTipo(TipoAgente tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
