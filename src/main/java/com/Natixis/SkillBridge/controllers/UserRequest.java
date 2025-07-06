package com.Natixis.SkillBridge.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import com.Natixis.SkillBridge.model.Document;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequest {
    // Superclass User fields
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "The user's name must be between 3 and 100 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Column(unique = true, nullable = false)
    private String email;
    private LocalDate registrationDate;
    private LocalTime registrationTime;
    @NotBlank(message = "Role is required")
    private String role; // Assuming role is a String, could be an enum or another type

    // Candidate specific fields
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
   
    private List<Document> documents;

    //Company specific fields
     @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{9}", message = "Phone number must have exactly 9 digits")
    @Column(unique = true, nullable = false)
    private String phone;
    @NotNull(message = "NIPC is required")
    @Digits(integer = 9, fraction = 0, message = "NIPC must be a 9-digit number")
    private int nipc;
    @NotNull(message = "Status is required")
    private int approvalStatus;

    // Default constructor
    public UserRequest() {
    }

    // Getters and Setters for User fields
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

      public LocalTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }


    // Getters and Setters for Candidate specific fields
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

  

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    // Getters and Setters for Company specific fields
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getNipc() {
        return nipc;
    }
    public void setNipc(int nipc) {
        this.nipc = nipc;
    }
    public int getApprovalStatus() {
        return approvalStatus;
    }
    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    
 
}
