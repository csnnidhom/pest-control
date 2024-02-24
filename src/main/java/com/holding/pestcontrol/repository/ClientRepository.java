package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByUserAndId(User user, Integer id);

    Optional<Client> findFirstByUserAndId(User user, Integer id);
}
