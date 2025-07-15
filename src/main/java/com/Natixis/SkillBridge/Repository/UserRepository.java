package com.Natixis.SkillBridge.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
