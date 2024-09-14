package com.SpringGateway.Spring.API.Gateway.credentialsRepo;


import com.SpringGateway.Spring.API.Gateway.entity.IpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpRepo extends JpaRepository<IpInfo, Integer> {

    List<IpInfo> getIpByClientId(String client_id);


}
