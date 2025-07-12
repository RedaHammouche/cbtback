package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Patient;
import com.cabinetdentaire.entity.RendezVous;
import com.cabinetdentaire.entity.RendezVous.StatutRendezVous;
import com.cabinetdentaire.repository.PatientRepository;
import com.cabinetdentaire.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {
    
    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private PatientRepository patientRepository; // Ajout de cette ligne

    public RendezVous creerRendezVous(RendezVous rendezVous, Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isPresent()) {
            rendezVous.setPatient(patientOptional.get());
            return rendezVousRepository.save(rendezVous);
        } else {
            throw new RuntimeException("Patient non trouvé avec l'ID: " + patientId);
        }
    }

    // Obtenir tous les rendez-vous
    public List<RendezVous> obtenirTousLesRendezVous() {
        return rendezVousRepository.findAll();
    }
    
    // Obtenir un rendez-vous par ID
    public Optional<RendezVous> obtenirRendezVousParId(Long id) {
        return rendezVousRepository.findById(id);
    }

    public RendezVous mettreAJourRendezVous(Long id, RendezVous rendezVousDetails, Long patientId) {
        Optional<RendezVous> rendezVousOptional = rendezVousRepository.findById(id);
        if (rendezVousOptional.isPresent()) {
            RendezVous rendezVous = rendezVousOptional.get();
            rendezVous.setDateHeure(rendezVousDetails.getDateHeure());
            rendezVous.setMotif(rendezVousDetails.getMotif());
            rendezVous.setStatut(rendezVousDetails.getStatut());

            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            if (patientOptional.isPresent()) {
                rendezVous.setPatient(patientOptional.get());
            } else {
                throw new RuntimeException("Patient non trouvé avec l'ID: " + patientId);
            }
            return rendezVousRepository.save(rendezVous);
        }
        return null;
    }


    // Supprimer un rendez-vous
    public boolean supprimerRendezVous(Long id) {
        if (rendezVousRepository.existsById(id)) {
            rendezVousRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Obtenir les rendez-vous d'un patient
    public List<RendezVous> obtenirRendezVousParPatient(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }
    
    // Obtenir les rendez-vous par statut
    public List<RendezVous> obtenirRendezVousParStatut(StatutRendezVous statut) {
        return rendezVousRepository.findByStatut(statut);
    }
    
    // Obtenir les rendez-vous d'une période
    public List<RendezVous> obtenirRendezVousParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return rendezVousRepository.findByDateHeureBetween(debut, fin);
    }
    
    // Obtenir les rendez-vous du jour
    public List<RendezVous> obtenirRendezVousDuJour(LocalDateTime date) {
        return rendezVousRepository.findByDate(date);
    }
    
    // Obtenir les prochains rendez-vous d'un patient
    public List<RendezVous> obtenirProchainRendezVousPatient(Long patientId) {
        return rendezVousRepository.findUpcomingByPatientId(patientId, LocalDateTime.now());
    }
    
    // Rechercher avec filtres
    public List<RendezVous> rechercherAvecFiltres(Long patientId, StatutRendezVous statut, 
                                                 LocalDateTime dateDebut, LocalDateTime dateFin) {
        return rendezVousRepository.findByFilters(patientId, statut, dateDebut, dateFin);
    }
    
    // Changer le statut d'un rendez-vous
    public RendezVous changerStatut(Long id, StatutRendezVous nouveauStatut) {
        Optional<RendezVous> rendezVousOptional = rendezVousRepository.findById(id);
        if (rendezVousOptional.isPresent()) {
            RendezVous rendezVous = rendezVousOptional.get();
            rendezVous.setStatut(nouveauStatut);
            return rendezVousRepository.save(rendezVous);
        }
        return null;
    }
}

