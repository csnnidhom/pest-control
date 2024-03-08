package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String>, JpaSpecificationExecutor<Worker> {
    Optional<Object> findByUser(User user);

    Optional<Object> findByUserAndId(User user, String id);

    Optional<Object> findByNamaLengkap(String namaLengkap);
}
