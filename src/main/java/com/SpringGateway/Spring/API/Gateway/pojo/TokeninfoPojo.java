package com.SpringGateway.Spring.API.Gateway.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokeninfoPojo{

    private String username;
    private String role;

}