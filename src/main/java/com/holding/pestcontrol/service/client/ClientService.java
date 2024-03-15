package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResClient;
import com.holding.pestcontrol.dto.response.ResponseSucces;
import com.holding.pestcontrol.dto.treatment.ReqResChangeStatusTreatment;
import com.holding.pestcontrol.dto.treatment.ReqResSuggestionTreatment;
import com.holding.pestcontrol.entity.Scheduling;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    ReqResClient getDetailProfile();
    ReqResClient updateDetailProfile(ReqResClient request);

    List<Scheduling> getAllScheduleClient(LocalDate startDate, LocalDate endDate);

    List<TreatmentDTO> getAllServiceTreatment(LocalDate startDate, LocalDate endDate);

    ReqResChangeStatusTreatment changeStatusTreatment(ReqResChangeStatusTreatment reqResChangeStatusTreatment);

    ReqResSuggestionTreatment createSuggestion(ReqResSuggestionTreatment reqResSuggestionTreatment);
}
