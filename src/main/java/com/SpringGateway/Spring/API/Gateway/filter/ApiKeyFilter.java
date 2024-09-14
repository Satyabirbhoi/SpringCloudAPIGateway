package com.SpringGateway.Spring.API.Gateway.filter;


import com.SpringGateway.Spring.API.Gateway.credentialsRepo.CredentialsRepo;
import com.SpringGateway.Spring.API.Gateway.entity.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Optional;



@Component
public class ApiKeyFilter implements GatewayFilter {

    @Autowired
    private CredentialsRepo credentialsRepo;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("filter started");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String fault = "{\"Error\": \"Unauthorized Access, Correct credentials required.\"}";

        HttpHeaders headers = request.getHeaders();

        String clientId = headers.getFirst("client_id");
        String secret = headers.getFirst("client_secret");

        System.out.println(clientId+"  "+secret);

        if (clientId != null && secret != null) {
            Optional<ClientInfo> clientDetails = credentialsRepo.getUsernameByClientId(clientId);
            if(clientDetails.isPresent()){
                    ClientInfo client = clientDetails.get();
                if (!client.getClientId().isEmpty()) {
                    if (client.getClientId().equals(clientId) && client.getClientSecret().equals(secret) && client.getEnabled()==1){
                        System.out.println("sucess");
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("apiusername", client.getApiusername())
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }
                    else if(client.getEnabled()==0){
                        fault  = "{\"Error\": \"Client is Disabled\"}";

                    }
                }
            }
            else{
                fault  = "{\"Error\": \"Client_id Not found\"}";
            }
        }
        else{
            fault  = "{\"Error\": \"Client_id or Client_secret missing\"}";
        }


        // String faultResponse = "{\"message\": \"Unauthorized\"}";
        Flux<DataBuffer> faultResponse = Flux.just(fault)
                .map(s -> exchange.getResponse().bufferFactory().wrap(s.getBytes()));

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);


        return response.writeWith(faultResponse);

        // return response.setComplete();


    }
}
