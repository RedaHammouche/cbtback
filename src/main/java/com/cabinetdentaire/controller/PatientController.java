package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.Patient;
import com.cabinetdentaire.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
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

    // Obtenir tous les patients
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

    // Rechercher des patients avec filtres étendus
    @GetMapping("/rechercher")
    public ResponseEntity<List<Patient>> rechercherPatients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) String typeAssurance) {
        try {
            List<Patient> patients;
            if (nom != null || prenom != null || email != null || cin != null || typeAssurance != null) {
                patients = patientService.rechercherAvecFiltres(nom, prenom, email, cin, typeAssurance);
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

    // Obtenir un patient par CIN
    @GetMapping("/cin/{cin}")
    public ResponseEntity<Patient> obtenirPatientParCin(@PathVariable("cin") String cin) {
        Optional<Patient> patient = patientService.obtenirPatientParCin(cin);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les patients par type d'assurance
    @GetMapping("/assurance/{typeAssurance}")
    public ResponseEntity<List<Patient>> obtenirPatientsParTypeAssurance(@PathVariable("typeAssurance") String typeAssurance) {
        try {
            List<Patient> patients = patientService.obtenirPatientsParTypeAssurance(typeAssurance);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
// Ajoutez cette méthode dans votre PatientController.java

    @GetMapping("/test")
    public ResponseEntity<String> testNouveauxChamps() {
        try {
            System.out.println("🧪 Test des nouveaux champs");

            // Créer un patient de test
            Patient testPatient = new Patient();
            testPatient.setNom("Test");
            testPatient.setPrenom("Patient");
            testPatient.setDateNaissance(LocalDate.of(1990, 1, 1));
            testPatient.setCin("TEST123");
            testPatient.setTypeAssurance("CNSS");

            System.out.println("Patient avant sauvegarde: " + testPatient);

            Patient saved = patientService.creerPatient(testPatient);
            System.out.println("Patient après sauvegarde: " + saved);

            return ResponseEntity.ok("Test réussi - ID: " + saved.getId() +
                    ", CIN: " + saved.getCin() +
                    ", Assurance: " + saved.getTypeAssurance());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }
    // Ajouter une consultation à l'historique
    @PostMapping("/{id}/consultations")
    public ResponseEntity<Patient> ajouterConsultation(@PathVariable("id") Long id,
                                                       @RequestBody LocalDate dateConsultation) {
        try {
            Patient patient = patientService.ajouterConsultation(id, dateConsultation);
            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}