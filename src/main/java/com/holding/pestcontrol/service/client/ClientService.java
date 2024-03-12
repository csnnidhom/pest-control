package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    ReqResClient getDetailProfile();
    ReqResClient updateDetailProfile(ReqResClient request);

    List<Scheduling> getAllScheduleClient(LocalDate startDate, LocalDate endDate);

}
