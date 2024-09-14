package com.SpringGateway.Spring.API.Gateway.filter;


import com.SpringGateway.Spring.API.Gateway.credentialsRepo.IpRepo;
import com.SpringGateway.Spring.API.Gateway.entity.IpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;




@Component
public class IpFilter implements GatewayFilter {

    @Autowired
    private IpRepo ipRepo;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String clientId = headers.getFirst("client_id");
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null) {
            String ipAddress = remoteAddress.getAddress().getHostAddress();
            if (!isAllowed(ipAddress, clientId)) {
                // IP not allowed, reject request
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }



    public boolean isAllowed(String ipAddress, String client_id) {
        List<IpInfo> ip = ipRepo.getIpByClientId(client_id);
        System.out.println(ip);
        System.out.println(ipAddress);
        for (IpInfo ipInfo : ip) {
            String storedIP = ipInfo.getIp();
            if (storedIP.equals(ipAddress)) {
                return true;
            }
        }
        return false;

    }
}