package com.Natixis.SkillBridge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Natixis.SkillBridge.Service.CandidateService;
import com.Natixis.SkillBridge.model.utilizador.Candidate;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<Candidate> criar(@RequestBody Candidate candidate) {
        Candidate salvo = candidateService.salvarCandidato(candidate);
        return ResponseEntity.ok(salvo);
    }
}
