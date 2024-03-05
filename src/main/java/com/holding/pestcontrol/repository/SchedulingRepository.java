package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, String> {

    List<Scheduling> findAllByWorker(Worker worker);
}
