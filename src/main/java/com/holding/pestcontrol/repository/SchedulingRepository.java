package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.Worker;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, String>, JpaSpecificationExecutor<Scheduling> {

    List<Scheduling> findAllByClientAndDateWorkingBetween(Client client,LocalDate startDate, LocalDate endDate);
}
