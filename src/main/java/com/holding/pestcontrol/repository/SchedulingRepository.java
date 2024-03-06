package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.service.worker.schedule.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, String>, JpaSpecificationExecutor<Scheduling> {

    List<Scheduling> findAllByWorker(Worker worker);

    List<Scheduling> findAllByWorkerAndClient(Worker worker, Client client);


}
