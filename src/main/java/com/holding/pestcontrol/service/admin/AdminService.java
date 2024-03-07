package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.enumm.WorkingType;

import java.util.List;

public interface AdminService {

    Role[] getRole();

    WorkingType[] getWorkingType();

    FreqType[] getFreqType();

    ReqResAdminCreateUpdate create(ReqResAdminCreateUpdate reqResAdminCreateUpdate);

    ReqResAdminCreateUpdate update(ReqResAdminCreateUpdate reqResAdminCreateUpdate);

    ReqResAdminGetDelete getDetailByEmail(ReqResAdminGetDelete reqResAdminGetDelete);

    List<User> getAllDataByRole(Role role);

    ReqResAdminGetDelete switchStatus(ReqResAdminGetDelete reqResAdminGetDelete);

    ReqResCreateScheduling createScheduling(ReqResCreateScheduling reqResCreateScheduling);

    List<Scheduling> getAllScheduling();
    ReqResUpdateScheduling updateScheduling(ReqResUpdateScheduling reqResUpdateScheduling);

    ReqResDeleteScheduling deleteSchduling(ReqResDeleteScheduling reqResDeleteScheduling);

    List<TreatmentDTO> getAllTreatment();
}
