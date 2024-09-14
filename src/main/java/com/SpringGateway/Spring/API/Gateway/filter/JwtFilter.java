package com.SpringGateway.Spring.API.Gateway.filter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class JwtFilter implements GatewayFilter {
    private static final byte[] FIXED_KEY = "qwSrtyuo9frtioart4bxjcvwcvkucvycv_uccyvTukjHxifuycdx_ytd".getBytes(StandardCharsets.UTF_8);


    private static final SecretKey key = Keys.hmacShaKeyFor(FIXED_KEY);

    public static String generateJWT(String username,String role) {
        return Jwts.builder()
                .setSubject("Authentication")
                .claim("username", username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public static Claims retrieveClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error parsing JWT: " + e.getMessage());
            return null;
        }
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("jwt filter started");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String fault = "{\"Error\": \"Unauthorized Access.\"}";

        HttpHeaders headers = request.getHeaders();

        String token = headers.getFirst("Authorization");


        System.out.println(token);

        if (token != null) {
            try{
            Claims claims = retrieveClaims(token);
            if (claims != null) {
                String username = ""+claims.get("username");
                String role = ""+claims.get("role");
                if (username != null && role != null){
                    System.out.println("retrived claims");
                    List<String> token_properties = new ArrayList<>();
                    token_properties.add("username : "+username);
                    token_properties.add("role : "+role);

                    System.out.println("sucess");
                    HttpHeaders.writableHttpHeaders(headers).remove("Authorization");
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("token_properties", String.valueOf(token_properties))
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
            }else {
                fault = "{\"Error\": \"Unauthorized Token.\"}";
            }
        }catch (Exception e){
                fault = "{\"Error\": \"Unauthorized Token.\"}";

            }
        }

        Flux<DataBuffer> faultResponse = Flux.just(fault)
                .map(s -> exchange.getResponse().bufferFactory().wrap(s.getBytes()));

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);


        return response.writeWith(faultResponse);

    }
}
