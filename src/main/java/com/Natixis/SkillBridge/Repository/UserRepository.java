package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.utilizador.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
