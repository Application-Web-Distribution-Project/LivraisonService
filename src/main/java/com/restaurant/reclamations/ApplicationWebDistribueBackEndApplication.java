package com.restaurant.reclamations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationWebDistribueBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationWebDistribueBackEndApplication.class, args);
        System.out.println(" Microservice Gestion des Réclamations démarré !");
    }

}
