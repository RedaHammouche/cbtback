package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.MouvementStock;
import com.cabinetdentaire.entity.Produit;
import com.cabinetdentaire.enums.TypeMouvement;
import com.cabinetdentaire.repository.MouvementStockRepository;
import com.cabinetdentaire.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MouvementStockService {

    private final MouvementStockRepository mouvementStockRepository;
    private final ProduitRepository produitRepository;

    public MouvementStockService(MouvementStockRepository mouvementStockRepository, ProduitRepository produitRepository) {
        this.mouvementStockRepository = mouvementStockRepository;
        this.produitRepository = produitRepository;
    }

    public List<MouvementStock> getAllMouvements() {
        return mouvementStockRepository.findAll();
    }

    @Transactional
    public MouvementStock enregistrerMouvement(MouvementStock mouvement) {
        // Validation du produit
        Produit produit = mouvement.getProduit();
        if (produit == null || produit.getId() == null) {
            throw new IllegalArgumentException("Produit invalide");
        }

        // Récupérer le produit complet
        Produit p = produitRepository.findById(produit.getId())
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        // Validation de la quantité
        if (mouvement.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        // Traitement selon le type de mouvement
        if (mouvement.getType() == TypeMouvement.ENTREE) {
            p.setQuantite(p.getQuantite() + mouvement.getQuantite());
        } else if (mouvement.getType() == TypeMouvement.SORTIE) {
            if (p.getQuantite() < mouvement.getQuantite()) {
                throw new IllegalArgumentException("Stock insuffisant. Stock actuel: " + p.getQuantite());
            }
            p.setQuantite(p.getQuantite() - mouvement.getQuantite());
        } else {
            throw new IllegalArgumentException("Type de mouvement invalide");
        }

        // Sauvegarder le produit mis à jour
        produitRepository.save(p);

        // Enregistrer le mouvement avec la date actuelle
        mouvement.setDate(LocalDateTime.now());
        mouvement.setProduit(p); // S'assurer que l'objet produit complet est associé

        return mouvementStockRepository.save(mouvement);
    }

    // Méthode corrigée pour vérifier l'existence de mouvements
    public boolean hasMovementsByProduitId(Long produitId) {
        // Compter les mouvements pour ce produit
        List<MouvementStock> mouvements = mouvementStockRepository.findAll();
        return mouvements.stream()
                .anyMatch(m -> m.getProduit() != null && m.getProduit().getId().equals(produitId));
    }
}