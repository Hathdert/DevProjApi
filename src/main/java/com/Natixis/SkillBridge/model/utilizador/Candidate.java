package com.Natixis.SkillBridge.model.utilizador;

import com.Natixis.SkillBridge.model.Document;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @NotNull(message = "Registration date is required")
    private LocalDate registrationDate;

    @NotNull(message = "Registration time is required")
    private LocalTime registrationTime;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
