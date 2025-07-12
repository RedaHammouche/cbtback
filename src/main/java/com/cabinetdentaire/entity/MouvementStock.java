package com.cabinetdentaire.entity;

import com.cabinetdentaire.enums.TypeMouvement;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TypeMouvement type;

    private int quantite;

    @ManyToOne
    private Produit produit;
}
