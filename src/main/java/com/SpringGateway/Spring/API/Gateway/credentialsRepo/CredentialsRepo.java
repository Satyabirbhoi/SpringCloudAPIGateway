package com.SpringGateway.Spring.API.Gateway.credentialsRepo;

import com.SpringGateway.Spring.API.Gateway.entity.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepo extends JpaRepository<ClientInfo, Integer> {

    Optional<ClientInfo> getUsernameByClientId(String client_id);

    Optional<ClientInfo> getClientInfoByApiusername(String client_id);


}
