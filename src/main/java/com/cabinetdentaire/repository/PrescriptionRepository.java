package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    // Recherche par consultation
    List<Prescription> findByConsultationId(Long consultationId);
    
    // Recherche par patient (via consultation)
    @Query("SELECT p FROM Prescription p WHERE p.consultation.patient.id = :patientId")
    List<Prescription> findByPatientId(@Param("patientId") Long patientId);
    
    // Recherche par médicament
    List<Prescription> findByMedicamentContainingIgnoreCase(String medicament);
    
    // Recherche avec filtres
    @Query("SELECT p FROM Prescription p WHERE " +
           "(:consultationId IS NULL OR p.consultation.id = :consultationId) AND " +
           "(:patientId IS NULL OR p.consultation.patient.id = :patientId) AND " +
           "(:medicament IS NULL OR LOWER(p.medicament) LIKE LOWER(CONCAT('%', :medicament, '%')))")
    List<Prescription> findByFilters(@Param("consultationId") Long consultationId,
                                   @Param("patientId") Long patientId,
                                   @Param("medicament") String medicament);
    
    // Statistiques - médicaments les plus prescrits
    @Query("SELECT p.medicament, COUNT(p) as nombre FROM Prescription p " +
           "GROUP BY p.medicament ORDER BY COUNT(p) DESC")
    List<Object[]> getMedicamentsLesPlusPrescrits();
}

