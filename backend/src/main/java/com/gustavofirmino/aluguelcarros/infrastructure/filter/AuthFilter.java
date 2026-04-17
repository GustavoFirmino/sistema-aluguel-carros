package com.gustavofirmino.aluguelcarros.infrastructure.filter;

import com.gustavofirmino.aluguelcarros.application.auth.AuthService;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Filtro de autenticação que valida o token Bearer em todas as rotas protegidas.
 *
 * Rotas públicas (sem necessidade de token):
 *   - POST /auth/login
 *   - GET  /health
 *   - GET  /
 *   - GET  /swagger-ui/**
 *   - GET  /swagger/**
 *   - OPTIONS /* (CORS preflight)
 */
@Filter(Filter.MATCH_ALL_PATTERN)
public class AuthFilter implements HttpServerFilter {

    private static final Set<String> ROTAS_PUBLICAS = Set.of(
            "/auth/login",
            "/auth/logout",
            "/auth/cadastro",
            "/health",
            "/"
    );

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String path = request.getPath();

        // Libera preflight CORS
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.proceed(request);
        }

        // Libera rotas públicas e Swagger UI
        if (ROTAS_PUBLICAS.contains(path)
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger")
                || path.startsWith("/rapidoc")
                // Catálogo de automóveis: qualquer pessoa pode ver sem login
                || (request.getMethod() == HttpMethod.GET && path.startsWith("/automoveis"))) {
            return chain.proceed(request);
        }

        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Publishers.just(unauthorized(request));
        }

        String token = authHeader.substring(7);
        if (authService.validarToken(token).isEmpty()) {
            return Publishers.just(unauthorized(request));
        }

        return chain.proceed(request);
    }

    /** 401 com headers CORS para que o browser não mascare o erro como CORS failure. */
    private MutableHttpResponse<?> unauthorized(HttpRequest<?> request) {
        String origin = request.getHeaders().get("Origin");
        MutableHttpResponse<?> response = HttpResponse.unauthorized();
        if (origin != null) {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Credentials", "true");
        }
        return response;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
