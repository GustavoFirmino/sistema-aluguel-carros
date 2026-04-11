package com.gustavofirmino.aluguelcarros.domain.model;

/**
 * Modelo de domínio que representa um agente do sistema.
 * Agentes são empresas ou bancos responsáveis por analisar e aprovar pedidos de aluguel.
 * Bancos também podem conceder contratos de crédito vinculados ao aluguel.
 */
public class Agente {

    private Long id;
    private String nome;
    private String cnpj;
    private TipoAgente tipo;
    private String email;
    private String telefone;

    public Agente() {}

    public Agente(Long id, String nome, String cnpj, TipoAgente tipo, String email, String telefone) {
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
