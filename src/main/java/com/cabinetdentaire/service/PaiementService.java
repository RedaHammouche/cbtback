package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Consultation;
import com.cabinetdentaire.entity.Paiement;
import com.cabinetdentaire.entity.Paiement.ModePaiement;
import com.cabinetdentaire.repository.ConsultationRepository;
import com.cabinetdentaire.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    // Créer un nouveau paiement
    public Paiement creerPaiement(Paiement paiement, Long consultationId) {
        Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
        if (consultationOptional.isPresent()) {
            paiement.setConsultation(consultationOptional.get());
            return paiementRepository.save(paiement);
        } else {
            throw new RuntimeException("Consultation non trouvée avec l'ID: " + consultationId);
        }
    }

    // Obtenir tous les paiements avec leurs consultations
    @Transactional(readOnly = true)
    public List<Paiement> obtenirTousLesPaiements() {
        return paiementRepository.findAllWithConsultations();
    }

    // Obtenir un paiement par ID
    @Transactional(readOnly = true)
    public Optional<Paiement> obtenirPaiementParId(Long id) {
        return paiementRepository.findByIdWithConsultation(id);
    }

    // Mettre à jour un paiement
    public Paiement mettreAJourPaiement(Long id, Paiement paiementDetails, Long consultationId) {
        Optional<Paiement> paiementOptional = paiementRepository.findById(id);
        if (paiementOptional.isPresent()) {
            Paiement paiement = paiementOptional.get();
            paiement.setMontant(paiementDetails.getMontant());
            paiement.setDatePaiement(paiementDetails.getDatePaiement());
            paiement.setModePaiement(paiementDetails.getModePaiement());

            // Mettre à jour la consultation
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
            if (consultationOptional.isPresent()) {
                paiement.setConsultation(consultationOptional.get());
            } else {
                throw new RuntimeException("Consultation non trouvée avec l'ID: " + consultationId);
            }

            return paiementRepository.save(paiement);
        }
        return null;
    }

    // Supprimer un paiement
    public boolean supprimerPaiement(Long id) {
        if (paiementRepository.existsById(id)) {
            paiementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtenir les paiements d'une consultation
    @Transactional(readOnly = true)
    public List<Paiement> obtenirPaiementsParConsultation(Long consultationId) {
        return paiementRepository.findByConsultationIdWithConsultation(consultationId);
    }

    // Obtenir les paiements d'un patient
    @Transactional(readOnly = true)
    public List<Paiement> obtenirPaiementsParPatient(Long patientId) {
        return paiementRepository.findByPatientIdWithConsultation(patientId);
    }

    // Obtenir les paiements par mode de paiement
    @Transactional(readOnly = true)
    public List<Paiement> obtenirPaiementsParMode(ModePaiement modePaiement) {
        return paiementRepository.findByModePaiementWithConsultation(modePaiement);
    }

    // Obtenir les paiements d'une période
    @Transactional(readOnly = true)
    public List<Paiement> obtenirPaiementsParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return paiementRepository.findByDatePaiementBetweenWithConsultation(debut, fin);
    }

    // Rechercher avec filtres
    @Transactional(readOnly = true)
    public List<Paiement> rechercherAvecFiltres(Long consultationId, Long patientId,
                                                ModePaiement modePaiement, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return paiementRepository.findByFiltersWithConsultation(consultationId, patientId, modePaiement, dateDebut, dateFin);
    }

    // Obtenir le total des paiements par mois
    @Transactional(readOnly = true)
    public List<Object[]> obtenirTotalPaiementsParMois(int annee) {
        return paiementRepository.getTotalPaiementsParMois(annee);
    }

    // Obtenir le total des paiements par mode
    @Transactional(readOnly = true)
    public List<Object[]> obtenirTotalPaiementsParMode() {
        return paiementRepository.getTotalPaiementsParMode();
    }
}