package com.SpringGateway.Spring.API.Gateway.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenResponse {

    private String status;
    private String message;

}