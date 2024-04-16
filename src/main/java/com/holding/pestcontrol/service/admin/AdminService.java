package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.entityDTO.SchedulingDTO;
import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminCreateDetailUser;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminGetDelete;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminUpdateDetailUser;
import com.holding.pestcontrol.dto.response.ResponseSearchClient;
import com.holding.pestcontrol.dto.schedule.ReqResCreateScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResDeleteScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResSchedulingData;
import com.holding.pestcontrol.dto.schedule.ReqResUpdateScheduling;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.enumm.WorkingType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    Role[] getRole();

    WorkingType[] getWorkingType();

    FreqType[] getFreqType();

    ReqResAdminCreateDetailUser createDetail(ReqResAdminCreateDetailUser reqResAdminCreateDetailUser);

    ReqResAdminUpdateDetailUser updateDetail(ReqResAdminUpdateDetailUser reqResAdminUpdateDetailUser);

    List<ResponseSearchClient> searchClient(String companyName, String companyAddress, String companyEmail);

    List<Worker> searchWorker(String workerFullname, String wokerAddress, String workerEmail);
    ReqResAdminGetDelete getDetailByEmail(String email);

    String switchStatus(String email);

    ReqResCreateScheduling createScheduling(ReqResCreateScheduling reqResCreateScheduling);

    List<Scheduling> searchSchedule(String companyName, LocalDate startDate, LocalDate endDate);

    SchedulingDTO getDataSchedulingById(String id);

    ReqResUpdateScheduling updateScheduling(ReqResUpdateScheduling reqResUpdateScheduling);

    ReqResDeleteScheduling deleteSchduling(ReqResDeleteScheduling reqResDeleteScheduling);

    List<TreatmentDTO> getAllTreatment(String companyName, LocalDate startDate, LocalDate endDate);

    String uploadFile(String companyName, MultipartFile file) throws IOException;

    byte[] downloadFile(String fileName);


}
