package com.cabinetdentaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import lombok.Data;
@Data
@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date et heure sont obligatoires")
    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @ManyToOne(fetch = FetchType.EAGER) // Changé en EAGER pour charger le patient
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut = StatutRendezVous.CONFIRME;

    // Suppression de la relation avec Consultation pour éviter les problèmes
    // @OneToOne(mappedBy = "rendezVous", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Consultation consultation;

    // Enum pour le statut
    public enum StatutRendezVous {
        CONFIRME, ANNULE, TERMINE, EN_ATTENTE
    }

    // Constructeurs
    public RendezVous() {}

    public RendezVous(LocalDateTime dateHeure, Patient patient, String motif, StatutRendezVous statut) {
        this.dateHeure = dateHeure;
        this.patient = patient;
        this.motif = motif;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public StatutRendezVous getStatut() {
        return statut;
    }

    public void setStatut(StatutRendezVous statut) {
        this.statut = statut;
    }
}