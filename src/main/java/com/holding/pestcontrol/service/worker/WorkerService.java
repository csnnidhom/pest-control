package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResWorker;
import com.holding.pestcontrol.dto.treatment.ReqResTreatmentCreate;
import com.holding.pestcontrol.dto.treatment.ReqResTreatmentUpdate;
import com.holding.pestcontrol.entity.Scheduling;

import java.time.LocalDate;
import java.util.List;

public interface WorkerService {
    ReqResWorker getDetailProfile();
    ReqResWorker updateDetailProfile(ReqResWorker request);

    List<Scheduling> getAllScheduleWorker(String companyname, LocalDate startDate, LocalDate endDate);

    ReqResTreatmentCreate createTreatment(ReqResTreatmentCreate reqResTreatmentCreate);

    List<TreatmentDTO> getAllServiceTreatment(String companyname, LocalDate startDate, LocalDate endDate);

    ReqResTreatmentUpdate updateTreatment(ReqResTreatmentUpdate reqResTreatmentUpdate);

    Re

}
