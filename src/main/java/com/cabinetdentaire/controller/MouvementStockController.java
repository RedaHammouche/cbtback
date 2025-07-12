package com.cabinetdentaire.controller;

import com.cabinetdentaire.entity.MouvementStock;
import com.cabinetdentaire.service.MouvementStockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements-stock")
@CrossOrigin
public class MouvementStockController {

    private final MouvementStockService service;

    public MouvementStockController(MouvementStockService service) {
        this.service = service;
    }

    @GetMapping
    public List<MouvementStock> getAll() {
        return service.getAllMouvements();
    }

    @PostMapping
    public MouvementStock enregistrer(@RequestBody MouvementStock mouvement) {
        return service.enregistrerMouvement(mouvement);
    }
}
