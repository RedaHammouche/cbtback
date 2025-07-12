package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Produit;
import com.cabinetdentaire.service.ProduitService;
import com.cabinetdentaire.service.MouvementStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin
public class ProduitController {

    private final ProduitService produitService;
    private final MouvementStockService mouvementStockService;

    public ProduitController(ProduitService produitService, MouvementStockService mouvementStockService) {
        this.produitService = produitService;
        this.mouvementStockService = mouvementStockService;
    }

    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        try {
            List<Produit> produits = produitService.getAllProduits();
            return ResponseEntity.ok(produits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduit(@PathVariable Long id) {
        try {
            Optional<Produit> produit = produitService.getProduitById(id);
            return produit.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduit(@RequestBody Produit produit) {
        try {
            // Validation métier
            if (produit.getNom() == null || produit.getNom().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Le nom du produit est obligatoire");
            }

            if (produit.getQuantite() == null || produit.getQuantite() < 0) {
                return ResponseEntity.badRequest().body("La quantité doit être positive ou nulle");
            }

            // Valeurs par défaut
            if (produit.getSeuilMinimal() == null) {
                produit.setSeuilMinimal(5);
            }

            // Nettoyer le nom
            produit.setNom(produit.getNom().trim());

            // Vérifier si un produit avec le même nom existe déjà
            if (produitService.existsByNom(produit.getNom())) {
                return ResponseEntity.badRequest().body("Un produit avec ce nom existe déjà");
            }

            Produit savedProduit = produitService.saveProduit(produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création du produit: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        try {
            // Vérifier si le produit existe
            Optional<Produit> existingProduit = produitService.getProduitById(id);
            if (existingProduit.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Validation métier
            if (produit.getNom() == null || produit.getNom().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Le nom du produit est obligatoire");
            }

            if (produit.getQuantite() == null || produit.getQuantite() < 0) {
                return ResponseEntity.badRequest().body("La quantité doit être positive ou nulle");
            }

            // Nettoyer le nom
            produit.setNom(produit.getNom().trim());

            // Vérifier si un autre produit avec le même nom existe déjà
            if (produitService.existsByNomAndIdNot(produit.getNom(), id)) {
                return ResponseEntity.badRequest().body("Un autre produit avec ce nom existe déjà");
            }

            produit.setId(id);
            Produit updatedProduit = produitService.saveProduit(produit);
            return ResponseEntity.ok(updatedProduit);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la modification du produit: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable Long id) {
        try {
            // Vérifier si le produit existe
            Optional<Produit> existingProduit = produitService.getProduitById(id);
            if (existingProduit.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Vérifier si le produit est utilisé dans des mouvements de stock
            if (mouvementStockService.hasMovementsByProduitId(id)) {
                return ResponseEntity.badRequest()
                        .body("Impossible de supprimer ce produit car il est utilisé dans l'historique des mouvements de stock");
            }

            produitService.deleteProduit(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du produit: " + e.getMessage());
        }
    }
}