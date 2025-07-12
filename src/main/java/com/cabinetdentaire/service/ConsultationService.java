package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Consultation;
import com.cabinetdentaire.entity.Patient;
import com.cabinetdentaire.entity.RendezVous;
import com.cabinetdentaire.repository.ConsultationRepository;
import com.cabinetdentaire.repository.PatientRepository;
import com.cabinetdentaire.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RendezVousRepository rendezVousRepository;

    // Créer une nouvelle consultation
    public Consultation creerConsultation(Consultation consultation, Long patientId, Long rendezVousId) {
        // Rechercher le patient
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isPresent()) {
            consultation.setPatient(patientOptional.get());

            // Rechercher le rendez-vous si fourni
            if (rendezVousId != null) {
                Optional<RendezVous> rendezVousOptional = rendezVousRepository.findById(rendezVousId);
                if (rendezVousOptional.isPresent()) {
                    consultation.setRendezVous(rendezVousOptional.get());
                }
            }

            return consultationRepository.save(consultation);
        } else {
            throw new RuntimeException("Patient non trouvé avec l'ID: " + patientId);
        }
    }

    // Obtenir toutes les consultations
    public List<Consultation> obtenirToutesLesConsultations() {
        return consultationRepository.findAll();
    }

    // Obtenir une consultation par ID
    public Optional<Consultation> obtenirConsultationParId(Long id) {
        return consultationRepository.findById(id);
    }

    // Mettre à jour une consultation
    public Consultation mettreAJourConsultation(Long id, Consultation consultationDetails, Long patientId, Long rendezVousId) {
        Optional<Consultation> consultationOptional = consultationRepository.findById(id);
        if (consultationOptional.isPresent()) {
            Consultation consultation = consultationOptional.get();
            consultation.setDateHeure(consultationDetails.getDateHeure());
            consultation.setDiagnostic(consultationDetails.getDiagnostic());
            consultation.setNotes(consultationDetails.getNotes());

            // Mettre à jour le patient
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            if (patientOptional.isPresent()) {
                consultation.setPatient(patientOptional.get());
            } else {
                throw new RuntimeException("Patient non trouvé avec l'ID: " + patientId);
            }

            // Mettre à jour le rendez-vous si fourni
            if (rendezVousId != null) {
                Optional<RendezVous> rendezVousOptional = rendezVousRepository.findById(rendezVousId);
                if (rendezVousOptional.isPresent()) {
                    consultation.setRendezVous(rendezVousOptional.get());
                } else {
                    consultation.setRendezVous(null);
                }
            } else {
                consultation.setRendezVous(null);
            }

            return consultationRepository.save(consultation);
        }
        return null;
    }

    // Supprimer une consultation
    public boolean supprimerConsultation(Long id) {
        if (consultationRepository.existsById(id)) {
            consultationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtenir les consultations d'un patient
    public List<Consultation> obtenirConsultationsParPatient(Long patientId) {
        return consultationRepository.findByPatientIdOrderByDateHeureDesc(patientId);
    }

    // Obtenir les consultations d'un rendez-vous
    public List<Consultation> obtenirConsultationsParRendezVous(Long rendezVousId) {
        return consultationRepository.findByRendezVousId(rendezVousId);
    }

    // Obtenir les consultations d'une période
    public List<Consultation> obtenirConsultationsParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return consultationRepository.findByDateHeureBetween(debut, fin);
    }

    // Obtenir les consultations du jour
    public List<Consultation> obtenirConsultationsDuJour(LocalDateTime date) {
        return consultationRepository.findByDate(date);
    }

    // Rechercher avec filtres
    public List<Consultation> rechercherAvecFiltres(Long patientId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return consultationRepository.findByFilters(patientId, dateDebut, dateFin);
    }

    // Obtenir les statistiques de consultations par mois
    public List<Object[]> obtenirStatistiquesConsultationsParMois(int annee) {
        return consultationRepository.getConsultationsParMois(annee);
    }
}