package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Recherche par nom et prénom
    List<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    // Recherche par email
    Optional<Patient> findByEmail(String email);
    
    // Recherche par téléphone
    Optional<Patient> findByTelephone(String telephone);
    
    // Recherche combinée
    @Query("SELECT p FROM Patient p WHERE " +
           "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:prenom IS NULL OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
           "(:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<Patient> findByFilters(@Param("nom") String nom, 
                               @Param("prenom") String prenom, 
                               @Param("email") String email);
}

