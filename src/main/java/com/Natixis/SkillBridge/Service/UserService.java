package com.Natixis.SkillBridge.Service;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.stereotype.Service;
import com.Natixis.SkillBridge.Repository.UserRepository;
import com.Natixis.SkillBridge.controllers.UserRequest;
import com.Natixis.SkillBridge.model.utilizador.Candidate;
import com.Natixis.SkillBridge.model.utilizador.Company;
import com.Natixis.SkillBridge.model.utilizador.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRequest request) {
     
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já em uso");
        }

        User user;

        switch (request.getRole().toLowerCase()) {
            case "candidate":
                Candidate c = new Candidate();
                c.setName(request.getName());
                c.setEmail(request.getEmail());
                c.setPassword(request.getPassword());
                c.setRole("candidate");
                c.setAddress(request.getAddress());
                c.setBirthDate(request.getBirthDate());
                c.setRegistrationDate(request.getRegistrationDate());
                c.setRegistrationTime(request.getRegistrationTime());
                c.setDocuments(request.getDocuments());
                user = c;
                break;
            case "company":
                Company comp = new Company();
                comp.setName(request.getName());
                comp.setEmail(request.getEmail());
                comp.setPassword(request.getPassword());
                comp.setRole("company");
                comp.setPhone(request.getPhone());
                comp.setNipc(request.getNipc());
                comp.setApprovalStatus(request.getApprovalStatus());
                user = comp;
                break;
            default:
                throw new RuntimeException("Role inválido");
        }

        userRepository.save(user);
    }
    }
   

