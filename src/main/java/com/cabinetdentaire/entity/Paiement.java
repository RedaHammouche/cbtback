package com.cabinetdentaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

@Data
@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Changé de LAZY à EAGER
    @JoinColumn(name = "consultation_id", nullable = false)
    @JsonManagedReference // Pour éviter les références circulaires
    private Consultation consultation;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    @Column(nullable = false)
    private Double montant;

    @NotNull(message = "La date de paiement est obligatoire")
    @Column(name = "date_paiement", nullable = false)
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", nullable = false)
    private ModePaiement modePaiement;

    // Enum pour le mode de paiement
    public enum ModePaiement {
        CARTE, ESPECES, CHEQUE, VIREMENT
    }

    // Constructeurs
    public Paiement() {}

    public Paiement(Consultation consultation, Double montant, LocalDateTime datePaiement, ModePaiement modePaiement) {
        this.consultation = consultation;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }
}