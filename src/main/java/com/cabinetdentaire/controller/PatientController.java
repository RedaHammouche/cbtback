package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Patient;
import com.cabinetdentaire.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    // Créer un nouveau patient
    @PostMapping
    public ResponseEntity<Patient> creerPatient(@Valid @RequestBody Patient patient) {
        try {
            Patient nouveauPatient = patientService.creerPatient(patient);
            return new ResponseEntity<>(nouveauPatient, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ajoutez ces logs dans votre PatientController.java
    @GetMapping
    public ResponseEntity<List<Patient>> obtenirTousLesPatients() {
        try {
            System.out.println("🔍 PatientController: obtenirTousLesPatients appelé");
            List<Patient> patients = patientService.obtenirTousLesPatients();
            System.out.println("📊 Nombre de patients trouvés: " + patients.size());
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Erreur dans PatientController: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Obtenir un patient par ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> obtenirPatientParId(@PathVariable("id") Long id) {
        Optional<Patient> patient = patientService.obtenirPatientParId(id);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Mettre à jour un patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> mettreAJourPatient(@PathVariable("id") Long id, 
                                                     @Valid @RequestBody Patient patient) {
        Patient patientMisAJour = patientService.mettreAJourPatient(id, patient);
        if (patientMisAJour != null) {
            return new ResponseEntity<>(patientMisAJour, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Supprimer un patient
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerPatient(@PathVariable("id") Long id) {
        try {
            boolean supprime = patientService.supprimerPatient(id);
            if (supprime) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Rechercher des patients
    @GetMapping("/rechercher")
    public ResponseEntity<List<Patient>> rechercherPatients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email) {
        try {
            List<Patient> patients;
            if (nom != null || prenom != null || email != null) {
                patients = patientService.rechercherAvecFiltres(nom, prenom, email);
            } else {
                patients = patientService.obtenirTousLesPatients();
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtenir un patient par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> obtenirPatientParEmail(@PathVariable("email") String email) {
        Optional<Patient> patient = patientService.obtenirPatientParEmail(email);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Obtenir un patient par téléphone
    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<Patient> obtenirPatientParTelephone(@PathVariable("telephone") String telephone) {
        Optional<Patient> patient = patientService.obtenirPatientParTelephone(telephone);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

