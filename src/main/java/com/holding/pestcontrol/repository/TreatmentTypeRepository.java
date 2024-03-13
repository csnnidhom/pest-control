package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.ServiceTreatmentSlip;
import com.holding.pestcontrol.entity.TreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentTypeRepository extends JpaRepository<TreatmentType, String> {

}
