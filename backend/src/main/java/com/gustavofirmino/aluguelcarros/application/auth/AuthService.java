package com.gustavofirmino.aluguelcarros.application.auth;

import com.gustavofirmino.aluguelcarros.dto.auth.AuthResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AuthService {

    private record UserCredential(String usuario, String senha, String nome, String role, Long clienteId) {}

    private static final List<UserCredential> USUARIOS_FIXOS = List.of(
            new UserCredential("admin",   "admin123",  "Administrador",    "admin",   null),
            new UserCredential("agente",  "agente123", "Agente Locadora",  "agente",  null),
            new UserCredential("cliente", "12345",     "Operador Clientes","cliente", 1L)
    );

    private final Map<String, UserCredential> usuariosDinamicos = new ConcurrentHashMap<>();
    private final Map<String, AuthSession> sessoes = new ConcurrentHashMap<>();
    private final AgenteRepository agenteRepository;

    public AuthService(AgenteRepository agenteRepository) {
        this.agenteRepository = agenteRepository;
    }

    public Optional<AuthResponseDTO> login(String usuario, String senha) {
        // 1. Verifica usuários fixos
        Optional<UserCredential> cred = USUARIOS_FIXOS.stream()
                .filter(u -> u.usuario().equals(usuario) && u.senha().equals(senha))
                .findFirst();

        if (cred.isPresent()) {
            UserCredential u = cred.get();
            String token = UUID.randomUUID().toString();
            sessoes.put(token, new AuthSession(u.usuario(), u.nome(), u.role(), null));
            return Optional.of(new AuthResponseDTO(token, u.usuario(), u.nome(), u.role(), u.clienteId()));
        }

        // 2. Verifica usuários dinâmicos (clientes registrados pelo site)
        Optional<UserCredential> dinamico = Optional.ofNullable(usuariosDinamicos.get(usuario))
                .filter(u -> u.senha().equals(senha));

        if (dinamico.isPresent()) {
            UserCredential u = dinamico.get();
            String token = UUID.randomUUID().toString();
            sessoes.put(token, new AuthSession(u.usuario(), u.nome(), u.role(), null));
            return Optional.of(new AuthResponseDTO(token, u.usuario(), u.nome(), u.role(), u.clienteId()));
        }

        // 3. Verifica agentes (banco / empresa) pelo campo login da entidade
        Optional<AgenteEntity> agente = agenteRepository.findByLogin(usuario)
                .filter(a -> a.getSenha() != null && a.getSenha().equals(senha));

        if (agente.isPresent()) {
            AgenteEntity a = agente.get();
            String role = a.getTipo().name().toLowerCase(); // "banco" ou "empresa"
            String token = UUID.randomUUID().toString();
            sessoes.put(token, new AuthSession(a.getLogin(), a.getNome(), role, a.getId()));
            return Optional.of(new AuthResponseDTO(token, a.getLogin(), a.getNome(), role, null, a.getId()));
        }

        return Optional.empty();
    }

    public boolean usuarioExiste(String usuario) {
        return USUARIOS_FIXOS.stream().anyMatch(u -> u.usuario().equals(usuario))
                || usuariosDinamicos.containsKey(usuario)
                || agenteRepository.findByLogin(usuario).isPresent();
    }

    public Optional<AuthResponseDTO> registrar(String usuario, String senha, String nome, Long clienteId) {
        if (usuarioExiste(usuario)) return Optional.empty();
        usuariosDinamicos.put(usuario, new UserCredential(usuario, senha, nome, "cliente", clienteId));
        String token = UUID.randomUUID().toString();
        sessoes.put(token, new AuthSession(usuario, nome, "cliente", null));
        return Optional.of(new AuthResponseDTO(token, usuario, nome, "cliente", clienteId));
    }

    public Optional<AuthSession> validarToken(String token) {
        return Optional.ofNullable(sessoes.get(token));
    }

    public void logout(String token) {
        sessoes.remove(token);
    }

    public record AuthSession(String usuario, String nome, String role, Long agenteId) {}
}
