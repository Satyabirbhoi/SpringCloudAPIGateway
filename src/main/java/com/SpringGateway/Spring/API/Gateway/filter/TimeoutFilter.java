package com.SpringGateway.Spring.API.Gateway.filter;

import io.netty.handler.timeout.TimeoutException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class TimeoutFilter extends AbstractGatewayFilterFactory<Duration> {

    public TimeoutFilter() {
        super(Duration.class);
    }

    @Override
    public GatewayFilter apply(Duration timeout) {

        return (exchange, chain) -> chain.filter(exchange).timeout(timeout)
                .onErrorResume(error -> {
                    if (error instanceof TimeoutException) {

                        exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        return exchange.getResponse().setComplete();
                    }
                    Flux<DataBuffer> faultResponse = Flux.just("{\"Error\": \"Gateway Timout.\"}")
                            .map(s -> exchange.getResponse().bufferFactory().wrap(s.getBytes()));
                    exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return exchange.getResponse().writeWith(faultResponse);

                });
    }
}
