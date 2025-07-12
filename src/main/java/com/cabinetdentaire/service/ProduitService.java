package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Produit;
import com.cabinetdentaire.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit saveProduit(Produit produit) {
        // Nettoyer les données avant sauvegarde
        if (produit.getNom() != null) {
            produit.setNom(produit.getNom().trim());
        }
        if (produit.getDescription() != null) {
            produit.setDescription(produit.getDescription().trim());
        }

        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public boolean existsByNom(String nom) {
        return produitRepository.existsByNomIgnoreCase(nom);
    }

    public boolean existsByNomAndIdNot(String nom, Long id) {
        return produitRepository.existsByNomIgnoreCaseAndIdNot(nom, id);
    }

    public List<Produit> searchProduits(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProduits();
        }
        return produitRepository.findByNomContainingIgnoreCase(query.trim());
    }

    // Méthodes optionnelles commentées pour éviter les erreurs
    // Vous pouvez les décommenter si vous ajoutez les méthodes correspondantes dans le repository

    /*
    public List<Produit> getProduitsWithLowStock() {
        return produitRepository.findProduitsWithLowStock();
    }

    public long countProduitsWithLowStock() {
        return produitRepository.countProduitsWithLowStock();
    }
    */
}