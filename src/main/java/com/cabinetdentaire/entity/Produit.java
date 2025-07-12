package com.cabinetdentaire.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private Integer quantite = 0;

    // Champs optionnels pour une meilleure gestion
    private String description;
    private Double prix;
    private Integer seuilMinimal = 5; // Seuil d'alerte par défaut
}