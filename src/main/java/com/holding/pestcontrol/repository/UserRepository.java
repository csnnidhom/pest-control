package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String Username);

    boolean existsByUsername(String username);
}
