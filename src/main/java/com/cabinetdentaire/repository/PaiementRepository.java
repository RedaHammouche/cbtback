package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.Paiement;
import com.cabinetdentaire.entity.Paiement.ModePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    // Recherche tous les paiements avec leurs consultations
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient")
    List<Paiement> findAllWithConsultations();

    // Recherche par ID avec consultation
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE p.id = :id")
    Optional<Paiement> findByIdWithConsultation(@Param("id") Long id);

    // Recherche par consultation avec consultation chargée
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE p.consultation.id = :consultationId")
    List<Paiement> findByConsultationIdWithConsultation(@Param("consultationId") Long consultationId);

    // Recherche par patient (via consultation) avec consultation chargée
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE c.patient.id = :patientId")
    List<Paiement> findByPatientIdWithConsultation(@Param("patientId") Long patientId);

    // Recherche par mode de paiement avec consultation chargée
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE p.modePaiement = :modePaiement")
    List<Paiement> findByModePaiementWithConsultation(@Param("modePaiement") ModePaiement modePaiement);

    // Recherche par date avec consultation chargée
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE p.datePaiement BETWEEN :debut AND :fin")
    List<Paiement> findByDatePaiementBetweenWithConsultation(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    // Recherche avec filtres et consultation chargée
    @Query("SELECT p FROM Paiement p LEFT JOIN FETCH p.consultation c LEFT JOIN FETCH c.patient WHERE " +
            "(:consultationId IS NULL OR p.consultation.id = :consultationId) AND " +
            "(:patientId IS NULL OR p.consultation.patient.id = :patientId) AND " +
            "(:modePaiement IS NULL OR p.modePaiement = :modePaiement) AND " +
            "(:dateDebut IS NULL OR p.datePaiement >= :dateDebut) AND " +
            "(:dateFin IS NULL OR p.datePaiement <= :dateFin)")
    List<Paiement> findByFiltersWithConsultation(@Param("consultationId") Long consultationId,
                                                 @Param("patientId") Long patientId,
                                                 @Param("modePaiement") ModePaiement modePaiement,
                                                 @Param("dateDebut") LocalDateTime dateDebut,
                                                 @Param("dateFin") LocalDateTime dateFin);

    // Méthodes originales conservées pour compatibilité
    List<Paiement> findByConsultationId(Long consultationId);

    @Query("SELECT p FROM Paiement p WHERE p.consultation.patient.id = :patientId")
    List<Paiement> findByPatientId(@Param("patientId") Long patientId);

    List<Paiement> findByModePaiement(ModePaiement modePaiement);

    List<Paiement> findByDatePaiementBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT p FROM Paiement p WHERE " +
            "(:consultationId IS NULL OR p.consultation.id = :consultationId) AND " +
            "(:patientId IS NULL OR p.consultation.patient.id = :patientId) AND " +
            "(:modePaiement IS NULL OR p.modePaiement = :modePaiement) AND " +
            "(:dateDebut IS NULL OR p.datePaiement >= :dateDebut) AND " +
            "(:dateFin IS NULL OR p.datePaiement <= :dateFin)")
    List<Paiement> findByFilters(@Param("consultationId") Long consultationId,
                                 @Param("patientId") Long patientId,
                                 @Param("modePaiement") ModePaiement modePaiement,
                                 @Param("dateDebut") LocalDateTime dateDebut,
                                 @Param("dateFin") LocalDateTime dateFin);

    // Statistiques - total des paiements par mois
    @Query("SELECT MONTH(p.datePaiement) as mois, SUM(p.montant) as total FROM Paiement p " +
            "WHERE YEAR(p.datePaiement) = :annee GROUP BY MONTH(p.datePaiement)")
    List<Object[]> getTotalPaiementsParMois(@Param("annee") int annee);

    // Statistiques - total des paiements par mode
    @Query("SELECT p.modePaiement, SUM(p.montant) as total FROM Paiement p GROUP BY p.modePaiement")
    List<Object[]> getTotalPaiementsParMode();
}