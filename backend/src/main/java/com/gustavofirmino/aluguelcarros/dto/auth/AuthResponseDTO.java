package com.gustavofirmino.aluguelcarros.dto.auth;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class AuthResponseDTO {

    private String token;
    private String usuario;
    private String nome;
    private String role;
    /** ID do ClienteEntity associado (preenchido apenas para role=cliente). */
    private Long clienteId;

    public AuthResponseDTO() {}

    /** ID do AgenteEntity associado (preenchido apenas para role=banco ou role=empresa). */
    private Long agenteId;

    public AuthResponseDTO(String token, String usuario, String nome, String role) {
        this.token = token;
        this.usuario = usuario;
        this.nome = nome;
        this.role = role;
    }

    public AuthResponseDTO(String token, String usuario, String nome, String role, Long clienteId) {
        this(token, usuario, nome, role);
        this.clienteId = clienteId;
    }

    public AuthResponseDTO(String token, String usuario, String nome, String role, Long clienteId, Long agenteId) {
        this(token, usuario, nome, role, clienteId);
        this.agenteId = agenteId;
    }

    public String getToken()    { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsuario()  { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getNome()     { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRole()     { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getClienteId()  { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getAgenteId()   { return agenteId; }
    public void setAgenteId(Long agenteId) { this.agenteId = agenteId; }
}
