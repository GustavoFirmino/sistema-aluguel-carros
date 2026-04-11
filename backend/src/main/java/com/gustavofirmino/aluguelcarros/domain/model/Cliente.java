package com.gustavofirmino.aluguelcarros.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String rg;
    private String endereco;
    private String profissao;
    private List<Empregador> empregadores = new ArrayList<>();

    public Cliente() {}

    public Cliente(Long id, String nome, String cpf, String rg, String endereco, String profissao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.profissao = profissao;
    }

    public Cliente(Long id, String nome, String cpf, String rg, String endereco, String profissao,
                   List<Empregador> empregadores) {
        this(id, nome, cpf, rg, endereco, profissao);
        this.empregadores = empregadores != null ? empregadores : new ArrayList<>();
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

    public List<Empregador> getEmpregadores() { return empregadores; }
    public void setEmpregadores(List<Empregador> empregadores) { this.empregadores = empregadores; }

    /** Value object para empregador dentro do domínio. */
    public static class Empregador {
        private String nomeEmpresa;
        private BigDecimal rendimento;

        public Empregador() {}
        public Empregador(String nomeEmpresa, BigDecimal rendimento) {
            this.nomeEmpresa = nomeEmpresa;
            this.rendimento = rendimento;
        }

        public String getNomeEmpresa() { return nomeEmpresa; }
        public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

        public BigDecimal getRendimento() { return rendimento; }
        public void setRendimento(BigDecimal rendimento) { this.rendimento = rendimento; }
    }
}
