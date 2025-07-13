package com.cabinetdentaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CabinetDentaireApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabinetDentaireApplication.class, args);

        // Message de confirmation
        System.out.println("=================================");
        System.out.println("🚀 Application démarrée !");
        System.out.println("📡 API disponible sur: http://localhost:8080");
        System.out.println("🏥 Cabinet Dentaire Backend");
        System.out.println("=================================");
    }
}