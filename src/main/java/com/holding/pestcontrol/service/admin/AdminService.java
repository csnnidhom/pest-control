package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.enumm.WorkingType;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    Role[] getRole();

    WorkingType[] getWorkingType();

    FreqType[] getFreqType();

    ReqResAdminCreateDetailUser create(ReqResAdminCreateDetailUser reqResAdminCreateDetailUser);

    ReqResAdminUpdateDetailUser update(ReqResAdminUpdateDetailUser reqResAdminUpdateDetailUser);

    List<Client> searchClient(String companyName, String companyAddress, String companyEmail);

    List<Worker> searchWorker(String workerFullname, String wokerAddress, String workerEmail);
    ReqResAdminGetDelete getDetailByEmail(ReqResAdminGetDelete reqResAdminGetDelete);

    ReqResAdminGetDelete switchStatus(ReqResAdminGetDelete reqResAdminGetDelete);

    ReqResCreateScheduling createScheduling(ReqResCreateScheduling reqResCreateScheduling);

    List<Scheduling> searchSchedule(String companyName, LocalDate startDate, LocalDate endDate);

    ReqResUpdateScheduling updateScheduling(ReqResUpdateScheduling reqResUpdateScheduling);

    ReqResDeleteScheduling deleteSchduling(ReqResDeleteScheduling reqResDeleteScheduling);

    List<TreatmentDTO> getAllTreatment();

}
