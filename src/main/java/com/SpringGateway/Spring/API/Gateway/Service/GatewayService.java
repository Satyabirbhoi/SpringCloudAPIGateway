package com.SpringGateway.Spring.API.Gateway.Service;

import com.SpringGateway.Spring.API.Gateway.credentialsRepo.CredentialsRepo;
import com.SpringGateway.Spring.API.Gateway.credentialsRepo.IpRepo;
import com.SpringGateway.Spring.API.Gateway.entity.ClientInfo;
import com.SpringGateway.Spring.API.Gateway.pojo.GenResponse;
import com.SpringGateway.Spring.API.Gateway.entity.IpInfo;
import com.SpringGateway.Spring.API.Gateway.filter.IpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class GatewayService{

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private IpRepo ipRepo;

    @Autowired
    private IpFilter ipFilter;


    public Optional<ClientInfo> addClient(ClientInfo clientInfo){
        clientInfo.setEnabled(1);
        credentialsRepo.save(clientInfo);
        return credentialsRepo.getUsernameByClientId(clientInfo.getClientId());
    }

    public GenResponse disable(String apiusername,int enable){

        Optional<ClientInfo> clientInfo = credentialsRepo.getClientInfoByApiusername(apiusername);
        try{

            if (clientInfo != null){
                clientInfo.get().setEnabled(enable);
            }
            GenResponse errorResponse = new GenResponse();
            errorResponse.setStatus("0");
            errorResponse.setMessage("API username "+clientInfo.get().getApiusername()+" status updated to "+enable);
            return (errorResponse);
        }catch (Exception e){
            GenResponse errorResponse = new GenResponse();
            errorResponse.setStatus("-1");
            errorResponse.setMessage("API username not found");
            return errorResponse;
        }

    }

    public GenResponse whitelist(IpInfo ipInfo){
        GenResponse response = new GenResponse();
        Optional<ClientInfo> clientInfo = credentialsRepo.getUsernameByClientId(ipInfo.getClientId());
        if (clientInfo.isPresent() && !ipFilter.isAllowed(ipInfo.getIp(),ipInfo.getClientId())) {
            ipRepo.save(ipInfo);
            response.setStatus("0");
            response.setMessage("Ip " + ipInfo.getIp() + " whitelisted for client " + ipInfo.getClientId());

            return response;
        }
        else {
                response.setStatus("-1");
                response.setMessage("Ip "+ipInfo.getIp()+" already whitelisted for the client or the client "+ipInfo.getClientId()+" not Found.");

                return response;
        }
    }




}