package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Méthodes de base nécessaires
    boolean existsByNomIgnoreCase(String nom);
    boolean existsByNomIgnoreCaseAndIdNot(String nom, Long id);
    List<Produit> findByNomContainingIgnoreCase(String nom);

    // Méthodes optionnelles (commentées pour éviter les erreurs)
    // Si vous voulez les utiliser plus tard, décommentez-les

    /*
    @Query("SELECT p FROM Produit p WHERE p.quantite <= COALESCE(p.seuilMinimal, 5)")
    List<Produit> findProduitsWithLowStock();

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.quantite <= COALESCE(p.seuilMinimal, 5)")
    long countProduitsWithLowStock();
    */
}