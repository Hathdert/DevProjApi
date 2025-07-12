package com.Natixis.SkillBridge.model.user;

import java.util.List;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.InternshipOffer;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Company extends User {

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{9}", message = "Phone number must have exactly 9 digits")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "Description is required")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "Area is required")
    private String area;

    @OneToMany(mappedBy = "company")
@JsonManagedReference("company-document")
private List<Document> documents;

    @OneToMany(mappedBy = "company")
@JsonManagedReference("company-internship")
private List<InternshipOffer> offers;

    @NotNull(message = "NIPC is required")
    @Digits(integer = 9, fraction = 0, message = "NIPC must be a 9-digit number")
    private int nipc;

    @NotNull(message = "Status is required")
    private int approvalStatus; // int representing approval status (0: pending, 1: approved, 2: rejected)

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public int getNipc() {
        return this.nipc;
    }

    public void setNipc(int nipc) {
        this.nipc = nipc;
    }

    public int getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
public List<InternshipOffer> getOffers() {
    return this.offers;
}

public void setOffers(List<InternshipOffer> offers) {
    this.offers = offers;
}
}
