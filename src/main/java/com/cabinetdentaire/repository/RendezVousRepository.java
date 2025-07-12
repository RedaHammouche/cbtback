package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.RendezVous;
import com.cabinetdentaire.entity.RendezVous.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    // Recherche par patient
    List<RendezVous> findByPatientId(Long patientId);
    
    // Recherche par statut
    List<RendezVous> findByStatut(StatutRendezVous statut);
    
    // Recherche par date
    List<RendezVous> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    
    // Recherche des rendez-vous du jour
    @Query("SELECT r FROM RendezVous r WHERE DATE(r.dateHeure) = DATE(:date)")
    List<RendezVous> findByDate(@Param("date") LocalDateTime date);
    
    // Recherche des rendez-vous à venir pour un patient
    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId AND r.dateHeure > :now ORDER BY r.dateHeure ASC")
    List<RendezVous> findUpcomingByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);
    
    // Recherche avec filtres
    @Query("SELECT r FROM RendezVous r WHERE " +
           "(:patientId IS NULL OR r.patient.id = :patientId) AND " +
           "(:statut IS NULL OR r.statut = :statut) AND " +
           "(:dateDebut IS NULL OR r.dateHeure >= :dateDebut) AND " +
           "(:dateFin IS NULL OR r.dateHeure <= :dateFin)")
    List<RendezVous> findByFilters(@Param("patientId") Long patientId,
                                  @Param("statut") StatutRendezVous statut,
                                  @Param("dateDebut") LocalDateTime dateDebut,
                                  @Param("dateFin") LocalDateTime dateFin);
}

