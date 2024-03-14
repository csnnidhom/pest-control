package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.profileUser.ReqResClient;
import com.holding.pestcontrol.entity.Scheduling;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    ReqResClient getDetailProfile();
    ReqResClient updateDetailProfile(ReqResClient request);

    List<Scheduling> getAllScheduleClient(LocalDate startDate, LocalDate endDate);

}
