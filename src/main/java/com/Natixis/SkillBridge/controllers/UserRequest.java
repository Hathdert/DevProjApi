package com.Natixis.SkillBridge.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import com.Natixis.SkillBridge.model.Document;

import jakarta.validation.constraints.NotNull;

public class UserRequest {
    // Superclass User fields
    private String password;
    private String name;
    private String email;
    private String role; // Assuming role is a String, could be an enum or another type

    // Candidate specific fields
    private String address;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private LocalTime registrationTime;
    private List<Document> documents;

    //Company specific fields
    private String phone;
    private String description;
    private String area;
    private int nipc;
    private int approvalStatus;

    // Default constructor
    public UserRequest() {
    }

    // Getters and Setters for User fields
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }

      public LocalTime getRegistrationTime() {
        return this.registrationTime;
    }

    public void setRegistrationTime(LocalTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }


    // Getters and Setters for Candidate specific fields
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    // Getters and Setters for Company specific fields
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
    
 
}
