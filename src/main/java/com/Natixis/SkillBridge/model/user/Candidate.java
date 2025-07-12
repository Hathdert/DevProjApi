package com.Natixis.SkillBridge.model.user;

import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.Document;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "candidate")
public class Candidate extends User {

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{9}", message = "Phone number must have exactly 9 digits")
    @Column(unique = true, nullable = false)
    private String phone;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "candidate")
@JsonManagedReference("candidate-application")
private List<Application> applications;

    @OneToMany(mappedBy = "candidate")
@JsonManagedReference("candidate-document")
private List<Document> documents;

    // Getters and Setters
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Application> getApplications() {
    return this.applications;
}

public void setApplications(List<Application> applications) {
    this.applications = applications;
}
}
