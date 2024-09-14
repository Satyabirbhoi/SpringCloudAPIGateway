package com.SpringGateway.Spring.API.Gateway.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse{

    private String token;
    private Date expiry;

}