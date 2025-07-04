package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.utilizador.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByName(String name);
    User findByEmailAndPassword(String email, String password);
    User findByEmailAndName(String email, String name);
    
}
