package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.ServiceTreatmentSlip;

import java.time.LocalDate;
import java.util.List;

public interface WorkerService {
    ReqResWorker getDetailProfile();
    ReqResWorker updateDetailProfile(ReqResWorker request);

    List<Scheduling> getAllScheduleWorker(String companyname, LocalDate startDate, LocalDate endDate);

    ReqResTreatment createTreatment(ReqResTreatment reqResTreatment);

    List<ServiceTreatmentSlip> getAllServiceTreatment(String companyname, LocalDate startDate, LocalDate endDate);

}
