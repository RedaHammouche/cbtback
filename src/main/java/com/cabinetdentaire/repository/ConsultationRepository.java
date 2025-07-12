package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
    // Recherche par patient
    List<Consultation> findByPatientIdOrderByDateHeureDesc(Long patientId);
    
    // Recherche par rendez-vous
    List<Consultation> findByRendezVousId(Long rendezVousId);
    
    // Recherche par date
    List<Consultation> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    
    // Recherche des consultations du jour
    @Query("SELECT c FROM Consultation c WHERE DATE(c.dateHeure) = DATE(:date)")
    List<Consultation> findByDate(@Param("date") LocalDateTime date);
    
    // Recherche avec filtres
    @Query("SELECT c FROM Consultation c WHERE " +
           "(:patientId IS NULL OR c.patient.id = :patientId) AND " +
           "(:dateDebut IS NULL OR c.dateHeure >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateHeure <= :dateFin)")
    List<Consultation> findByFilters(@Param("patientId") Long patientId,
                                   @Param("dateDebut") LocalDateTime dateDebut,
                                   @Param("dateFin") LocalDateTime dateFin);
    
    // Statistiques - nombre de consultations par mois
    @Query("SELECT MONTH(c.dateHeure) as mois, COUNT(c) as nombre FROM Consultation c " +
           "WHERE YEAR(c.dateHeure) = :annee GROUP BY MONTH(c.dateHeure)")
    List<Object[]> getConsultationsParMois(@Param("annee") int annee);
}

