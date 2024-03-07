package com.holding.pestcontrol.service.worker.treatment;

import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.entity.ServiceTreatmentSlip;

import java.util.List;
import java.util.Optional;

public interface Treatment {

    ReqResTreatment createTreatment(ReqResTreatment reqResTreatment);

}
