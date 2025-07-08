package com.Natixis.SkillBridge.model;

import com.Natixis.SkillBridge.model.user.Candidate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Candidate is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @NotNull(message = "Pitch cannot be null")
    @NotBlank(message = "Pitch cannot be empty")
    @Size(min = 10, max = 500, message = "Pitch must be between 10 and 500 characters")
    private String pitch;

    @NotNull(message = "State is required")
    @Min(0)
    @Max(2)
    public int state; // 0 - pending, 1 - accepted, 2 - rejected

    // Getters and Setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getPitch() {
        return this.pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
