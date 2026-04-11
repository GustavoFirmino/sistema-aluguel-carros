package com.gustavofirmino.aluguelcarros.dto.agente;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class AgenteResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private TipoAgente tipo;
    private String email;
    private String telefone;

    public AgenteResponseDTO() {}

    public AgenteResponseDTO(Long id, String nome, String cnpj, TipoAgente tipo, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public TipoAgente getTipo() { return tipo; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
