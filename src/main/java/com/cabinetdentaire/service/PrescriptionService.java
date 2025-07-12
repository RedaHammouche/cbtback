package com.cabinetdentaire.service;

import com.cabinetdentaire.entity.Consultation;
import com.cabinetdentaire.entity.Prescription;
import com.cabinetdentaire.repository.ConsultationRepository;
import com.cabinetdentaire.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    // Créer une nouvelle prescription
    public Prescription creerPrescription(Prescription prescription, Long consultationId) {
        Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
        if (consultationOptional.isPresent()) {
            prescription.setConsultation(consultationOptional.get());
            return prescriptionRepository.save(prescription);
        } else {
            throw new RuntimeException("Consultation non trouvée avec l'ID: " + consultationId);
        }
    }

    // Obtenir toutes les prescriptions
    public List<Prescription> obtenirToutesLesPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Obtenir une prescription par ID
    public Optional<Prescription> obtenirPrescriptionParId(Long id) {
        return prescriptionRepository.findById(id);
    }

    // Mettre à jour une prescription
    public Prescription mettreAJourPrescription(Long id, Prescription prescriptionDetails, Long consultationId) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            Prescription prescription = prescriptionOptional.get();
            prescription.setMedicament(prescriptionDetails.getMedicament());
            prescription.setDosage(prescriptionDetails.getDosage());
            prescription.setInstructions(prescriptionDetails.getInstructions());

            // Mettre à jour la consultation
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
            if (consultationOptional.isPresent()) {
                prescription.setConsultation(consultationOptional.get());
            } else {
                throw new RuntimeException("Consultation non trouvée avec l'ID: " + consultationId);
            }

            return prescriptionRepository.save(prescription);
        }
        return null;
    }

    // Supprimer une prescription
    public boolean supprimerPrescription(Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtenir les prescriptions d'une consultation
    public List<Prescription> obtenirPrescriptionsParConsultation(Long consultationId) {
        return prescriptionRepository.findByConsultationId(consultationId);
    }

    // Obtenir les prescriptions d'un patient
    public List<Prescription> obtenirPrescriptionsParPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    // Rechercher des prescriptions par médicament
    public List<Prescription> rechercherParMedicament(String medicament) {
        return prescriptionRepository.findByMedicamentContainingIgnoreCase(medicament);
    }

    // Rechercher avec filtres
    public List<Prescription> rechercherAvecFiltres(Long consultationId, Long patientId, String medicament) {
        return prescriptionRepository.findByFilters(consultationId, patientId, medicament);
    }

    // Obtenir les médicaments les plus prescrits
    public List<Object[]> obtenirMedicamentsLesPlusPrescrits() {
        return prescriptionRepository.getMedicamentsLesPlusPrescrits();
    }
}