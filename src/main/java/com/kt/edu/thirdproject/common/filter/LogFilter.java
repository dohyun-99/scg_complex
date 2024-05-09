package com.kt.edu.thirdproject.common.filter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.swing.*;
import java.util.UUID;

@Slf4j
@Component
@CircuitBreaker(name="apigw")
public class LogFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();

        //  Global No 생성을 위해서 uuid 사용
        String uuid = UUID.randomUUID().toString();

        //  Header에 데이터를 넣기 위해서는 mutate 사용
        exchange.getRequest().mutate().header("🍎GLOBAL_NO",uuid);
        log.info("🍎REQUEST GLOBAL NO [{}]", uuid);

        log.info("🍎Serving '{}'", path);

        return chain.filter(exchange)
                .doAfterTerminate(() -> {
                            //exchange.getResponse() => ServerHttpResponse
                            //exchange.getResponse().getHeaders() => HttpHeaders (Map)
                            //exchange.getResponse().getHeaders().entrySet() => Set<Map.Entry<String, List<String>>>
                            exchange.getResponse().getHeaders().entrySet().forEach(e ->
                                    log.info("🍏Response Headers '{}' : '{}'",e.getKey(),e.getValue()));
                            log.info("🍏Served '{}' as {} in {} ms",
                                    path,
                                    exchange.getResponse().getStatusCode(),
                                    System.currentTimeMillis() - startTime);
                        }
                );
    }
}