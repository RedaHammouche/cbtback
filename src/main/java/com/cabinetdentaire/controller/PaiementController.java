package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Paiement;
import com.cabinetdentaire.entity.Paiement.ModePaiement;
import com.cabinetdentaire.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/paiements")
@CrossOrigin(origins = "*")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    // Test endpoint simple
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API Paiements fonctionne !");
    }

    // Obtenir tous les paiements avec debug complet
    @GetMapping
    public ResponseEntity<?> obtenirTousLesPaiements() {
        try {
            System.out.println("=== DEBUT GET /api/paiements ===");

            // Vérifier que le service existe
            if (paiementService == null) {
                System.out.println("ERREUR: PaiementService est null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Service non initialisé");
            }

            System.out.println("Service OK, récupération des paiements...");
            List<Paiement> paiements = paiementService.obtenirTousLesPaiements();

            System.out.println("Nombre de paiements trouvés: " + paiements.size());

            // Log de chaque paiement
            for (int i = 0; i < paiements.size(); i++) {
                Paiement p = paiements.get(i);
                System.out.println("Paiement " + i + ":");
                System.out.println("  ID: " + p.getId());
                System.out.println("  Montant: " + p.getMontant());
                System.out.println("  Date: " + p.getDatePaiement());
                System.out.println("  Mode: " + p.getModePaiement());

                if (p.getConsultation() != null) {
                    System.out.println("  Consultation ID: " + p.getConsultation().getId());
                    if (p.getConsultation().getPatient() != null) {
                        System.out.println("  Patient: " + p.getConsultation().getPatient().getNom());
                    } else {
                        System.out.println("  Patient: null");
                    }
                } else {
                    System.out.println("  Consultation: null");
                }
            }

            System.out.println("=== Envoi de la réponse ===");
            return new ResponseEntity<>(paiements, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("=== ERREUR dans obtenirTousLesPaiements ===");
            System.out.println("Type d'erreur: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("=== Stack trace complète ===");
            e.printStackTrace();
            System.out.println("=== FIN Stack trace ===");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // Créer un nouveau paiement
    @PostMapping
    public ResponseEntity<?> creerPaiement(@RequestBody Map<String, Object> requestBody) {
        try {
            System.out.println("=== DEBUT POST /api/paiements ===");
            System.out.println("Body reçu: " + requestBody);

            // Extraction des données du body
            Long consultationId = Long.valueOf(requestBody.get("consultationId").toString());
            Double montant = Double.valueOf(requestBody.get("montant").toString());
            String datePaiement = requestBody.get("datePaiement").toString();
            String modePaiementStr = requestBody.get("modePaiement").toString();

            System.out.println("Données extraites:");
            System.out.println("  consultationId: " + consultationId);
            System.out.println("  montant: " + montant);
            System.out.println("  datePaiement: " + datePaiement);
            System.out.println("  modePaiement: " + modePaiementStr);

            // Création de l'objet Paiement
            Paiement paiement = new Paiement();
            paiement.setMontant(montant);
            paiement.setDatePaiement(LocalDateTime.parse(datePaiement));
            paiement.setModePaiement(ModePaiement.valueOf(modePaiementStr));

            System.out.println("Objet Paiement créé, appel du service...");
            Paiement nouveauPaiement = paiementService.creerPaiement(paiement, consultationId);

            System.out.println("Paiement créé avec succès, ID: " + nouveauPaiement.getId());
            return new ResponseEntity<>(nouveauPaiement, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("=== ERREUR dans creerPaiement ===");
            System.out.println("Type d'erreur: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // Obtenir un paiement par ID
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> obtenirPaiementParId(@PathVariable("id") Long id) {
        Optional<Paiement> paiement = paiementService.obtenirPaiementParId(id);
        if (paiement.isPresent()) {
            return new ResponseEntity<>(paiement.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour un paiement
    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourPaiement(@PathVariable("id") Long id,
                                                 @RequestBody Map<String, Object> requestBody) {
        try {
            System.out.println("=== DEBUT PUT /api/paiements/" + id + " ===");

            // Extraction des données du body
            Long consultationId = Long.valueOf(requestBody.get("consultationId").toString());
            Double montant = Double.valueOf(requestBody.get("montant").toString());
            String datePaiement = requestBody.get("datePaiement").toString();
            String modePaiementStr = requestBody.get("modePaiement").toString();

            // Création de l'objet Paiement avec les nouvelles données
            Paiement paiement = new Paiement();
            paiement.setMontant(montant);
            paiement.setDatePaiement(LocalDateTime.parse(datePaiement));
            paiement.setModePaiement(ModePaiement.valueOf(modePaiementStr));

            Paiement paiementMisAJour = paiementService.mettreAJourPaiement(id, paiement, consultationId);
            if (paiementMisAJour != null) {
                return new ResponseEntity<>(paiementMisAJour, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("=== ERREUR dans mettreAJourPaiement ===");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // Supprimer un paiement
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerPaiement(@PathVariable("id") Long id) {
        try {
            boolean supprime = paiementService.supprimerPaiement(id);
            if (supprime) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Autres méthodes restent identiques...
    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<Paiement>> obtenirPaiementsParConsultation(@PathVariable("consultationId") Long consultationId) {
        try {
            List<Paiement> paiements = paiementService.obtenirPaiementsParConsultation(consultationId);
            return new ResponseEntity<>(paiements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Paiement>> obtenirPaiementsParPatient(@PathVariable("patientId") Long patientId) {
        try {
            List<Paiement> paiements = paiementService.obtenirPaiementsParPatient(patientId);
            return new ResponseEntity<>(paiements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mode/{mode}")
    public ResponseEntity<List<Paiement>> obtenirPaiementsParMode(@PathVariable("mode") ModePaiement mode) {
        try {
            List<Paiement> paiements = paiementService.obtenirPaiementsParMode(mode);
            return new ResponseEntity<>(paiements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rechercher")
    public ResponseEntity<List<Paiement>> rechercherPaiements(
            @RequestParam(required = false) Long consultationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) ModePaiement modePaiement,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        try {
            List<Paiement> paiements = paiementService.rechercherAvecFiltres(consultationId, patientId, modePaiement, dateDebut, dateFin);
            return new ResponseEntity<>(paiements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistiques/mois/{annee}")
    public ResponseEntity<List<Object[]>> obtenirTotalPaiementsParMois(@PathVariable("annee") int annee) {
        try {
            List<Object[]> statistiques = paiementService.obtenirTotalPaiementsParMois(annee);
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistiques/modes")
    public ResponseEntity<List<Object[]>> obtenirTotalPaiementsParMode() {
        try {
            List<Object[]> statistiques = paiementService.obtenirTotalPaiementsParMode();
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}