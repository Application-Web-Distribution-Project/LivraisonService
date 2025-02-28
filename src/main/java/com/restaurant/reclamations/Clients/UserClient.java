package com.restaurant.reclamations.Clients;

import com.restaurant.reclamations.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "https://a0c9188a-cb4c-4aeb-8eb4-ccc70641b175.mock.pstmn.io/users")
public interface UserClient {
    @GetMapping(value = "/{id}", produces = "application/json", headers = "Accept=application/json")
    UserDTO getUserById(@PathVariable("id") Long id);

}
