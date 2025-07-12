package com.cabinetdentaire.repository;

import com.cabinetdentaire.entity.MouvementStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
}
