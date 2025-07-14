package com.cabinetdentaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    private String adresse;
    private String telephone;

    @Email(message = "L'email doit être valide")
    private String email;

    // NOUVEAUX CHAMPS
    private String cin;

    @Column(name = "type_assurance")
    private String typeAssurance;

    @ElementCollection
    @CollectionTable(name = "patient_consultations", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "date_consultation")
    private List<LocalDate> historiqueConsultations;

    // Constructeurs
    public Patient() {}

    public Patient(String nom, String prenom, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    // TOUS LES GETTERS ET SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // NOUVEAUX GETTERS/SETTERS
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTypeAssurance() { return typeAssurance; }
    public void setTypeAssurance(String typeAssurance) { this.typeAssurance = typeAssurance; }

    public List<LocalDate> getHistoriqueConsultations() { return historiqueConsultations; }
    public void setHistoriqueConsultations(List<LocalDate> historiqueConsultations) {
        this.historiqueConsultations = historiqueConsultations;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cin='" + cin + '\'' +
                ", typeAssurance='" + typeAssurance + '\'' +
                '}';
    }
}