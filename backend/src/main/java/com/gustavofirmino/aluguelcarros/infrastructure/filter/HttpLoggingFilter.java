package com.gustavofirmino.aluguelcarros.infrastructure.filter;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filtro de logging HTTP que registra método, path, status e tempo de resposta
 * para cada requisição processada pela API.
 *
 * Exemplo de saída:
 *   [HTTP] POST /pedidos      → 201  (47 ms)
 *   [HTTP] GET  /clientes     → 200  (12 ms)
 *   [HTTP] GET  /pedidos/999  → 404  (8 ms)
 */
@Filter(Filter.MATCH_ALL_PATTERN)
public class HttpLoggingFilter implements HttpServerFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        long inicio = System.currentTimeMillis();
        String method = request.getMethod().name();
        String path   = request.getPath();

        Publisher<MutableHttpResponse<?>> upstream = chain.proceed(request);

        return subscriber -> upstream.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscriber.onSubscribe(s);
            }

            @Override
            public void onNext(MutableHttpResponse<?> response) {
                long duracao = System.currentTimeMillis() - inicio;
                log.info("[HTTP] {} {}  ->  {}  ({} ms)", method, path,
                        response.getStatus().getCode(), duracao);
                subscriber.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                long duracao = System.currentTimeMillis() - inicio;
                log.error("[HTTP] {} {}  ->  ERROR  ({} ms) - {}", method, path, duracao, t.getMessage());
                subscriber.onError(t);
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        });
    }

    @Override
    public int getOrder() {
        return 10; // executa depois do AuthFilter (ordem 0)
    }
}
