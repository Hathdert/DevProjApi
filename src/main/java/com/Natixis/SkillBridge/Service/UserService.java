package com.Natixis.SkillBridge.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Natixis.SkillBridge.Repository.UserRepository;
import com.Natixis.SkillBridge.controllers.UserRequest;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.model.user.User;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public String getUserRole(String email) {
        User user = findByEmail(email);
        if (user != null) {
            if(user instanceof Candidate) {
                return "1";
            } else if (user instanceof Company) {
                return "2";
            }
            return "0"; 
        }
        return null;
    }

    public void registerUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail already in use");
        }

        User user;
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        switch (request.getRole().toLowerCase()) {
            case "candidate":
                Candidate c = new Candidate();
                c.setName(request.getName());
                c.setEmail(request.getEmail());
                c.setPassword(passwordEncoder.encode(request.getPassword()));
                c.setPhone(request.getPhone());
                c.setRole("candidate");
                c.setAddress(request.getAddress());
                c.setBirthDate(request.getBirthDate());
                c.setRegistrationDate(today);
                c.setRegistrationTime(now);
                c.setDocuments(request.getDocuments());
                user = c;
                break;
            case "company":
                Company comp = new Company();
                comp.setName(request.getName());
                comp.setAddress(request.getAddress());
                comp.setEmail(request.getEmail());
                comp.setPassword(passwordEncoder.encode(request.getPassword()));
                comp.setRole("company");
                comp.setPhone(request.getPhone());
                comp.setDescription(request.getDescription());
                comp.setArea(request.getArea());
                comp.setNipc(request.getNipc());
                comp.setApprovalStatus(request.getApprovalStatus());
                comp.setRegistrationDate(today);
                comp.setRegistrationTime(now);
                comp.setApprovalStatus(request.getApprovalStatus());
                user = comp;
                break;
            default:
                throw new RuntimeException("Invalid Role ");
        }

        userRepository.save(user);

        // emailService.sendWelcomeEmail(user.getEmail(), user.getName());

    }
}
