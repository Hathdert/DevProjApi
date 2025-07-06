package com.Natixis.SkillBridge.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import com.Natixis.SkillBridge.model.Document;

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
