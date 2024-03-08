package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Scheduling;
import java.time.LocalDate;
import java.util.List;

public interface WorkerService {
    ReqResWorker getDetailProfile();
    ReqResWorker updateDetailProfile(ReqResWorker request);

    List<Scheduling> getAllSchedule();

    ReqResTreatment createTreatment(ReqResTreatment reqResTreatment);

}
