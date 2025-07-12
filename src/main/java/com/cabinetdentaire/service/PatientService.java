package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Patient;
import com.cabinetdentaire.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Créer un nouveau patient
    public Patient creerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> obtenirTousLesPatients() {
        System.out.println("🔍 PatientService: obtenirTousLesPatients appelé");
        List<Patient> patients = patientRepository.findAll();
        System.out.println("📊 Repository a retourné: " + patients.size() + " patients");
        return patients;
    }

    // Obtenir un patient par ID
    public Optional<Patient> obtenirPatientParId(Long id) {
        return patientRepository.findById(id);
    }

    // Mettre à jour un patient
    public Patient mettreAJourPatient(Long id, Patient patientDetails) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patient.setNom(patientDetails.getNom());
            patient.setPrenom(patientDetails.getPrenom());
            patient.setDateNaissance(patientDetails.getDateNaissance());
            patient.setAdresse(patientDetails.getAdresse());
            patient.setTelephone(patientDetails.getTelephone());
            patient.setEmail(patientDetails.getEmail());
            return patientRepository.save(patient);
        }
        return null;
    }

    // Supprimer un patient
    public boolean supprimerPatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Rechercher des patients par nom ou prénom
    public List<Patient> rechercherPatients(String nom, String prenom) {
        return patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom);
    }

    // Rechercher un patient par email
    public Optional<Patient> obtenirPatientParEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    // Rechercher un patient par téléphone
    public Optional<Patient> obtenirPatientParTelephone(String telephone) {
        return patientRepository.findByTelephone(telephone);
    }

    // Rechercher avec filtres
    public List<Patient> rechercherAvecFiltres(String nom, String prenom, String email) {
        return patientRepository.findByFilters(nom, prenom, email);
    }
}

