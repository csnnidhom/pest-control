package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.ServiceTreatmentSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTreatmenSlipRepository extends JpaRepository<ServiceTreatmentSlip, String> {

}
