package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Prescription;
import com.cabinetdentaire.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // Créer une nouvelle prescription
    @PostMapping
    public ResponseEntity<Prescription> creerPrescription(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            Long consultationId = Long.valueOf(requestBody.get("consultationId").toString());
            String medicament = requestBody.get("medicament").toString();
            String dosage = requestBody.get("dosage") != null ?
                    requestBody.get("dosage").toString() : "";
            String instructions = requestBody.get("instructions") != null ?
                    requestBody.get("instructions").toString() : "";

            // Création de l'objet Prescription
            Prescription prescription = new Prescription();
            prescription.setMedicament(medicament);
            prescription.setDosage(dosage);
            prescription.setInstructions(instructions);

            Prescription nouvellePrescription = prescriptionService.creerPrescription(prescription, consultationId);
            return new ResponseEntity<>(nouvellePrescription, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir toutes les prescriptions
    @GetMapping
    public ResponseEntity<List<Prescription>> obtenirToutesLesPrescriptions() {
        try {
            List<Prescription> prescriptions = prescriptionService.obtenirToutesLesPrescriptions();
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir une prescription par ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> obtenirPrescriptionParId(@PathVariable("id") Long id) {
        Optional<Prescription> prescription = prescriptionService.obtenirPrescriptionParId(id);
        if (prescription.isPresent()) {
            return new ResponseEntity<>(prescription.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour une prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> mettreAJourPrescription(@PathVariable("id") Long id,
                                                                @RequestBody Map<String, Object> requestBody) {
        try {
            // Extraction des données du body
            Long consultationId = Long.valueOf(requestBody.get("consultationId").toString());
            String medicament = requestBody.get("medicament").toString();
            String dosage = requestBody.get("dosage") != null ?
                    requestBody.get("dosage").toString() : "";
            String instructions = requestBody.get("instructions") != null ?
                    requestBody.get("instructions").toString() : "";

            // Création de l'objet Prescription avec les nouvelles données
            Prescription prescription = new Prescription();
            prescription.setMedicament(medicament);
            prescription.setDosage(dosage);
            prescription.setInstructions(instructions);

            Prescription prescriptionMiseAJour = prescriptionService.mettreAJourPrescription(id, prescription, consultationId);
            if (prescriptionMiseAJour != null) {
                return new ResponseEntity<>(prescriptionMiseAJour, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer une prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerPrescription(@PathVariable("id") Long id) {
        try {
            boolean supprime = prescriptionService.supprimerPrescription(id);
            if (supprime) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les prescriptions d'une consultation
    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<Prescription>> obtenirPrescriptionsParConsultation(@PathVariable("consultationId") Long consultationId) {
        try {
            List<Prescription> prescriptions = prescriptionService.obtenirPrescriptionsParConsultation(consultationId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les prescriptions d'un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> obtenirPrescriptionsParPatient(@PathVariable("patientId") Long patientId) {
        try {
            List<Prescription> prescriptions = prescriptionService.obtenirPrescriptionsParPatient(patientId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Rechercher des prescriptions par médicament
    @GetMapping("/medicament/{medicament}")
    public ResponseEntity<List<Prescription>> rechercherParMedicament(@PathVariable("medicament") String medicament) {
        try {
            List<Prescription> prescriptions = prescriptionService.rechercherParMedicament(medicament);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Rechercher avec filtres
    @GetMapping("/rechercher")
    public ResponseEntity<List<Prescription>> rechercherPrescriptions(
            @RequestParam(required = false) Long consultationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String medicament) {
        try {
            List<Prescription> prescriptions = prescriptionService.rechercherAvecFiltres(consultationId, patientId, medicament);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtenir les médicaments les plus prescrits
    @GetMapping("/statistiques/medicaments-populaires")
    public ResponseEntity<List<Object[]>> obtenirMedicamentsLesPlusPrescrits() {
        try {
            List<Object[]> statistiques = prescriptionService.obtenirMedicamentsLesPlusPrescrits();
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}