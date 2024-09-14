package com.SpringGateway.Spring.API.Gateway.RestController;


import com.SpringGateway.Spring.API.Gateway.Service.GatewayService;

import com.SpringGateway.Spring.API.Gateway.entity.*;

import com.SpringGateway.Spring.API.Gateway.filter.JwtFilter;
import com.SpringGateway.Spring.API.Gateway.pojo.GenResponse;
import com.SpringGateway.Spring.API.Gateway.pojo.TokenResponse;
import com.SpringGateway.Spring.API.Gateway.pojo.TokeninfoPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;




@RestController
@RequestMapping("/apiGateway")
public class Restcontroller{


    @Autowired
    private GatewayService service;

    @Autowired
    private JwtFilter jwtFilter;


    @PostMapping("/addNewClient")
    public Optional<ClientInfo> addNewClient(@RequestBody ClientInfo clientInfo) {

        return service.addClient(clientInfo);
    }


    @PostMapping("/whitelist")
    public ResponseEntity<GenResponse> whitelistIP(@RequestBody IpInfo ipInfo) {
        GenResponse response = service.whitelist(ipInfo);

        if(response.getStatus().equals("0")){

         return ResponseEntity.ok(response);
       }
       else{
           return ResponseEntity.badRequest().body(response);
       }
    }


    @PostMapping("/disable_enable")
    public GenResponse disable(@RequestBody ClientInfo clientInfo) {

        return service.disable(clientInfo.getApiusername(),clientInfo.getEnabled());
    }


    @PostMapping("/generateJWT")
    public TokenResponse generateJWT(@RequestBody TokeninfoPojo tokeninfoPojo) {

        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setToken(jwtFilter.generateJWT(tokeninfoPojo.getUsername(),tokeninfoPojo.getRole()));
        tokenResponse.setExpiry(new Date(System.currentTimeMillis() + 3600000));
        return tokenResponse;


    }





}