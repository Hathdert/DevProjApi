package com.Natixis.SkillBridge.model;

import java.util.List;

import com.Natixis.SkillBridge.model.user.Candidate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @JsonBackReference("candidate-application")
    private Candidate candidate;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("application-document")
    private List<Document> document;

    @ManyToOne
    @JoinColumn(name = "internship_offer_id")
    @JsonBackReference("offer-application")
    private InternshipOffer internshipOffer;

    @NotNull(message = "Pitch cannot be null")
    @NotBlank(message = "Pitch cannot be empty")
    @Size(min = 10, max = 500, message = "Pitch must be between 10 and 500 characters")
    private String pitch;

    @NotNull(message = "State is required")
    @Min(0)
    @Max(2)
    private int state; // 0 - pending, 1 - accepted, 2 - rejected

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

    public List<Document> getDocument() {
        return this.document;
    }

    public void setDocument(List<Document> document) {
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

    public InternshipOffer getInternshipOffer() {
        return this.internshipOffer;
    }

    public void setInternshipOffer(InternshipOffer internshipOffer) {
        this.internshipOffer = internshipOffer;
    }
}
