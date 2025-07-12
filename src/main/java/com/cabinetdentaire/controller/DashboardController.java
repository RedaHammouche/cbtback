package com.cabinetdentaire.controller;

import com.cabinetdentaire.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private RendezVousService rendezVousService;
    
    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private PaiementService paiementService;
    
    // Obtenir les statistiques générales du tableau de bord
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> obtenirStatistiquesGenerales() {
        try {
            Map<String, Object> statistiques = new HashMap<>();
            
            // Nombre total de patients
            long totalPatients = patientService.obtenirTousLesPatients().size();
            statistiques.put("totalPatients", totalPatients);
            
            // Nombre de rendez-vous aujourd'hui
            long rendezVousAujourdhui = rendezVousService.obtenirRendezVousDuJour(LocalDateTime.now()).size();
            statistiques.put("rendezVousAujourdhui", rendezVousAujourdhui);
            
            // Nombre de consultations aujourd'hui
            long consultationsAujourdhui = consultationService.obtenirConsultationsDuJour(LocalDateTime.now()).size();
            statistiques.put("consultationsAujourdhui", consultationsAujourdhui);
            
            // Nombre total de consultations
            long totalConsultations = consultationService.obtenirToutesLesConsultations().size();
            statistiques.put("totalConsultations", totalConsultations);
            
            // Nombre total de prescriptions
            long totalPrescriptions = prescriptionService.obtenirToutesLesPrescriptions().size();
            statistiques.put("totalPrescriptions", totalPrescriptions);
            
            // Nombre total de paiements
            long totalPaiements = paiementService.obtenirTousLesPaiements().size();
            statistiques.put("totalPaiements", totalPaiements);
            
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtenir les statistiques de revenus
    @GetMapping("/revenus/{annee}")
    public ResponseEntity<Map<String, Object>> obtenirStatistiquesRevenus(@PathVariable("annee") int annee) {
        try {
            Map<String, Object> statistiques = new HashMap<>();
            
            // Revenus par mois
            statistiques.put("revenusParMois", paiementService.obtenirTotalPaiementsParMois(annee));
            
            // Revenus par mode de paiement
            statistiques.put("revenusParMode", paiementService.obtenirTotalPaiementsParMode());
            
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtenir les statistiques d'activité
    @GetMapping("/activite/{annee}")
    public ResponseEntity<Map<String, Object>> obtenirStatistiquesActivite(@PathVariable("annee") int annee) {
        try {
            Map<String, Object> statistiques = new HashMap<>();
            
            // Consultations par mois
            statistiques.put("consultationsParMois", consultationService.obtenirStatistiquesConsultationsParMois(annee));
            
            // Médicaments les plus prescrits
            statistiques.put("medicamentsPopulaires", prescriptionService.obtenirMedicamentsLesPlusPrescrits());
            
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

