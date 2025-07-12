package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Consultation;
import com.cabinetdentaire.service.ConsultationService;
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
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    // Créer une nouvelle consultation
    @PostMapping
    public ResponseEntity<Consultation> creerConsultation(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            String dateHeure = requestBody.get("dateHeure").toString();
            Long patientId = Long.valueOf(requestBody.get("patientId").toString());
            Long rendezVousId = requestBody.get("rendezVousId") != null ?
                    Long.valueOf(requestBody.get("rendezVousId").toString()) : null;
            String diagnostic = requestBody.get("diagnostic") != null ?
                    requestBody.get("diagnostic").toString() : "";
            String notes = requestBody.get("notes") != null ?
                    requestBody.get("notes").toString() : "";

            // Création de l'objet Consultation
            Consultation consultation = new Consultation();
            consultation.setDateHeure(LocalDateTime.parse(dateHeure));
            consultation.setDiagnostic(diagnostic);
            consultation.setNotes(notes);

            Consultation nouvelleConsultation = consultationService.creerConsultation(consultation, patientId, rendezVousId);
            return new ResponseEntity<>(nouvelleConsultation, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir toutes les consultations
    @GetMapping
    public ResponseEntity<List<Consultation>> obtenirToutesLesConsultations() {
        try {
            List<Consultation> consultations = consultationService.obtenirToutesLesConsultations();
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir une consultation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Consultation> obtenirConsultationParId(@PathVariable("id") Long id) {
        Optional<Consultation> consultation = consultationService.obtenirConsultationParId(id);
        if (consultation.isPresent()) {
            return new ResponseEntity<>(consultation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une consultation
    @PutMapping("/{id}")
    public ResponseEntity<Consultation> mettreAJourConsultation(@PathVariable("id") Long id,
                                                                @RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            String dateHeure = requestBody.get("dateHeure").toString();
            Long patientId = Long.valueOf(requestBody.get("patientId").toString());
            Long rendezVousId = requestBody.get("rendezVousId") != null ?
                    Long.valueOf(requestBody.get("rendezVousId").toString()) : null;
            String diagnostic = requestBody.get("diagnostic") != null ?
                    requestBody.get("diagnostic").toString() : "";
            String notes = requestBody.get("notes") != null ?
                    requestBody.get("notes").toString() : "";

            // Création de l'objet Consultation avec les nouvelles données
            Consultation consultation = new Consultation();
            consultation.setDateHeure(LocalDateTime.parse(dateHeure));
            consultation.setDiagnostic(diagnostic);
            consultation.setNotes(notes);

            Consultation consultationMiseAJour = consultationService.mettreAJourConsultation(id, consultation, patientId, rendezVousId);
            if (consultationMiseAJour != null) {
                return new ResponseEntity<>(consultationMiseAJour, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer une consultation
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerConsultation(@PathVariable("id") Long id) {
        try {
            boolean supprime = consultationService.supprimerConsultation(id);
            if (supprime) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les consultations d'un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Consultation>> obtenirConsultationsParPatient(@PathVariable("patientId") Long patientId) {
        try {
            List<Consultation> consultations = consultationService.obtenirConsultationsParPatient(patientId);
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les consultations d'un rendez-vous
    @GetMapping("/rendez-vous/{rendezVousId}")
    public ResponseEntity<List<Consultation>> obtenirConsultationsParRendezVous(@PathVariable("rendezVousId") Long rendezVousId) {
        try {
            List<Consultation> consultations = consultationService.obtenirConsultationsParRendezVous(rendezVousId);
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les consultations du jour
    @GetMapping("/aujourd-hui")
    public ResponseEntity<List<Consultation>> obtenirConsultationsAujourdhui() {
        try {
            List<Consultation> consultations = consultationService.obtenirConsultationsDuJour(LocalDateTime.now());
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Rechercher avec filtres
    @GetMapping("/rechercher")
    public ResponseEntity<List<Consultation>> rechercherConsultations(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        try {
            List<Consultation> consultations = consultationService.rechercherAvecFiltres(patientId, dateDebut, dateFin);
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les statistiques de consultations par mois
    @GetMapping("/statistiques/mois/{annee}")
    public ResponseEntity<List<Object[]>> obtenirStatistiquesConsultationsParMois(@PathVariable("annee") int annee) {
        try {
            List<Object[]> statistiques = consultationService.obtenirStatistiquesConsultationsParMois(annee);
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}