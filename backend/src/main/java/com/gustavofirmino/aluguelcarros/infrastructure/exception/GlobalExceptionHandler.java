package com.gustavofirmino.aluguelcarros.infrastructure.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;

/**
 * Handler global de exceções que garante respostas HTTP padronizadas em toda a API.
 *
 * Mapeamento:
 *   BusinessException           → 422 Unprocessable Entity
 *   ResourceNotFoundException   → 404 Not Found
 *   ConstraintViolationException → tratado pelo handler embutido do Micronaut Validation
 */
public class GlobalExceptionHandler {

    @Serdeable
    public record ErrorResponse(
            int status,
            String error,
            String message,
            String path,
            LocalDateTime timestamp) {
    }

    @Singleton
    @Produces
    @Requires(classes = BusinessException.class)
    public static class BusinessExceptionHandler
            implements ExceptionHandler<BusinessException, HttpResponse<ErrorResponse>> {

        @Override
        public HttpResponse<ErrorResponse> handle(HttpRequest request, BusinessException exception) {
            ErrorResponse body = new ErrorResponse(
                    HttpStatus.UNPROCESSABLE_ENTITY.getCode(),
                    "Regra de negócio violada",
                    exception.getMessage(),
                    request.getPath(),
                    LocalDateTime.now());
            return HttpResponse.<ErrorResponse>status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
        }
    }

    @Singleton
    @Produces
    @Requires(classes = ResourceNotFoundException.class)
    public static class ResourceNotFoundExceptionHandler
            implements ExceptionHandler<ResourceNotFoundException, HttpResponse<ErrorResponse>> {

        @Override
        public HttpResponse<ErrorResponse> handle(HttpRequest request, ResourceNotFoundException exception) {
            ErrorResponse body = new ErrorResponse(
                    HttpStatus.NOT_FOUND.getCode(),
                    "Recurso não encontrado",
                    exception.getMessage(),
                    request.getPath(),
                    LocalDateTime.now());
            return HttpResponse.<ErrorResponse>status(HttpStatus.NOT_FOUND).body(body);
        }
    }

}
