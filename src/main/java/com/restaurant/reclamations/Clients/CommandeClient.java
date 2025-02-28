package com.restaurant.reclamations.Clients;

import com.restaurant.reclamations.DTO.CommandeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "commande-service", url = "https://a0c9188a-cb4c-4aeb-8eb4-ccc70641b175.mock.pstmn.io/commandes")
public interface CommandeClient {

    @GetMapping("/{id}")
    CommandeDTO getCommandeById(@PathVariable("id") Long id);
}
