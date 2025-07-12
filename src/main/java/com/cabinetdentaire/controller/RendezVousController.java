package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.RendezVous;
import com.cabinetdentaire.entity.RendezVous.StatutRendezVous;
import com.cabinetdentaire.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rendez-vous")
@CrossOrigin(origins = "*")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    // Créer un nouveau rendez-vous - Méthode corrigée
    @PostMapping
    public ResponseEntity<RendezVous> creerRendezVous(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            Long patientId = Long.valueOf(requestBody.get("patientId").toString());
            String dateHeure = requestBody.get("dateHeure").toString();
            String motif = requestBody.get("motif") != null ? requestBody.get("motif").toString() : "";
            String statutStr = requestBody.get("statut") != null ? requestBody.get("statut").toString() : "CONFIRME";

            // Création de l'objet RendezVous
            RendezVous rendezVous = new RendezVous();
            rendezVous.setDateHeure(LocalDateTime.parse(dateHeure));
            rendezVous.setMotif(motif);
            rendezVous.setStatut(StatutRendezVous.valueOf(statutStr));

            RendezVous nouveauRendezVous = rendezVousService.creerRendezVous(rendezVous, patientId);
            return new ResponseEntity<>(nouveauRendezVous, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Pour le débogage
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir tous les rendez-vous
    @GetMapping
    public ResponseEntity<List<RendezVous>> obtenirTousLesRendezVous() {
        try {
            List<RendezVous> rendezVous = rendezVousService.obtenirTousLesRendezVous();
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir un rendez-vous par ID
    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> obtenirRendezVousParId(@PathVariable("id") Long id) {
        Optional<RendezVous> rendezVous = rendezVousService.obtenirRendezVousParId(id);
        if (rendezVous.isPresent()) {
            return new ResponseEntity<>(rendezVous.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour un rendez-vous - Méthode corrigée
    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> mettreAJourRendezVous(@PathVariable("id") Long id,
                                                            @RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            Long patientId = Long.valueOf(requestBody.get("patientId").toString());
            String dateHeure = requestBody.get("dateHeure").toString();
            String motif = requestBody.get("motif") != null ? requestBody.get("motif").toString() : "";
            String statutStr = requestBody.get("statut") != null ? requestBody.get("statut").toString() : "CONFIRME";

            // Création de l'objet RendezVous avec les nouvelles données
            RendezVous rendezVous = new RendezVous();
            rendezVous.setDateHeure(LocalDateTime.parse(dateHeure));
            rendezVous.setMotif(motif);
            rendezVous.setStatut(StatutRendezVous.valueOf(statutStr));

            RendezVous rendezVousMisAJour = rendezVousService.mettreAJourRendezVous(id, rendezVous, patientId);
            if (rendezVousMisAJour != null) {
                return new ResponseEntity<>(rendezVousMisAJour, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un rendez-vous
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerRendezVous(@PathVariable("id") Long id) {
        try {
            boolean supprime = rendezVousService.supprimerRendezVous(id);
            if (supprime) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les rendez-vous d'un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVous>> obtenirRendezVousParPatient(@PathVariable("patientId") Long patientId) {
        try {
            List<RendezVous> rendezVous = rendezVousService.obtenirRendezVousParPatient(patientId);
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les rendez-vous par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<RendezVous>> obtenirRendezVousParStatut(@PathVariable("statut") StatutRendezVous statut) {
        try {
            List<RendezVous> rendezVous = rendezVousService.obtenirRendezVousParStatut(statut);
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les rendez-vous du jour
    @GetMapping("/aujourd-hui")
    public ResponseEntity<List<RendezVous>> obtenirRendezVousAujourdhui() {
        try {
            List<RendezVous> rendezVous = rendezVousService.obtenirRendezVousDuJour(LocalDateTime.now());
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Rechercher avec filtres
    @GetMapping("/rechercher")
    public ResponseEntity<List<RendezVous>> rechercherRendezVous(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) StatutRendezVous statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        try {
            List<RendezVous> rendezVous = rendezVousService.rechercherAvecFiltres(patientId, statut, dateDebut, dateFin);
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Changer le statut d'un rendez-vous
    @PatchMapping("/{id}/statut")
    public ResponseEntity<RendezVous> changerStatut(@PathVariable("id") Long id,
                                                    @RequestParam StatutRendezVous statut) {
        RendezVous rendezVous = rendezVousService.changerStatut(id, statut);
        if (rendezVous != null) {
            return new ResponseEntity<>(rendezVous, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}