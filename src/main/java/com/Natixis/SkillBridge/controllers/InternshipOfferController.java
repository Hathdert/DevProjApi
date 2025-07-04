package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.InternshipOfferService;
import com.Natixis.SkillBridge.model.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internshipoffers")
@CrossOrigin(origins = "http://localhost:4200")  // Ajuste a URL do front Angular se for diferente
public class InternshipOfferController {

    @Autowired
    private InternshipOfferService service;

    // Listar todas as ofertas
    @GetMapping
    public List<InternshipOffer> getAllOffers() {
        return service.findAll();
    }

    // Buscar uma oferta por id
    @GetMapping("/{id}")
    public ResponseEntity<InternshipOffer> getOfferById(@PathVariable Long id) {
        Optional<InternshipOffer> offerOpt = service.findById(id);
        return offerOpt.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar uma nova oferta
    @PostMapping
    public ResponseEntity<InternshipOffer> createOffer(@RequestBody InternshipOffer offer) {
        InternshipOffer savedOffer = service.save(offer);
        return ResponseEntity.ok(savedOffer);
    }

    // Atualizar uma oferta existente
    @PutMapping("/{id}")
    public ResponseEntity<InternshipOffer> updateOffer(@PathVariable Long id, @RequestBody InternshipOffer offer) {
        InternshipOffer updatedOffer = service.update(id, offer);
        if (updatedOffer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOffer);
    }

    // Deletar uma oferta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
