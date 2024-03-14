package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.ServiceTreatmentSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTreatmenSlipRepository extends JpaRepository<ServiceTreatmentSlip, String>, JpaSpecificationExecutor<ServiceTreatmentSlip> {

    boolean existsByScheduling(Scheduling scheduling);

}
