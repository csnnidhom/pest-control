package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.entityDTO.SchedulingDTO;
import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminCreateDetailUser;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminGetDelete;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminUpdateDetailUser;
import com.holding.pestcontrol.dto.response.ResponseSearchClient;
import com.holding.pestcontrol.dto.response.ResponseSucces;
import com.holding.pestcontrol.dto.schedule.ReqResCreateScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResDeleteScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResUpdateScheduling;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.enumm.WorkingType;
import com.holding.pestcontrol.service.admin.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    @GetMapping("/get-role")
    public Role[] getRole(){
        return adminServiceImpl.getRole();
    }

    @GetMapping("/get-working-type")
    public WorkingType[] getWorkingType(){
        return adminServiceImpl.getWorkingType();
    }

    @GetMapping("/get-freq-type")
    public FreqType[] getFreqType(){
        return adminServiceImpl.getFreqType();
    }

    @PostMapping("/create-detail-user")
    public ReqResAdminCreateDetailUser create(@RequestBody ReqResAdminCreateDetailUser request){
        return adminServiceImpl.createDetail(request);
    }

    @PutMapping("/update-detail-user")
    public ReqResAdminUpdateDetailUser update(@RequestBody ReqResAdminUpdateDetailUser request){
        return adminServiceImpl.updateDetail(request);
    }

    @GetMapping("/search-client")
    public List<ResponseSearchClient> searchClient(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String companyAddress,
            @RequestParam(required = false) String companyEmail
    )
    {
        return adminServiceImpl.searchClient(companyName, companyAddress, companyEmail);
    }

    @GetMapping("/search-worker")
    public List<Worker> searchWorker(
            @RequestParam(required = false) String workerFullname,
            @RequestParam(required = false) String workerAddress,
            @RequestParam(required = false) String workerEmail
    )
    {
        return adminServiceImpl.searchWorker(workerFullname, workerAddress, workerEmail);
    }

    @GetMapping("/get-detail-by-email")
    public ReqResAdminGetDelete get(@RequestParam String email){
        return adminServiceImpl.getDetailByEmail(email);
    }

    @PatchMapping("/switch-status")
    public ResponseSucces<String> switchStatus(@RequestParam String email){
        String respon = adminServiceImpl.switchStatus(email);
        return ResponseSucces.<String>builder().data(respon).build();
    }

    @PostMapping("/create-scheduling")
    public ReqResCreateScheduling create(@RequestBody ReqResCreateScheduling request){
        return adminServiceImpl.createScheduling(request);
    }

    @GetMapping("/get-all-schedule")
    public List<Scheduling> searchSchedule(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    )
    {
        return adminServiceImpl.searchSchedule(companyName, startDate, endDate);
    }

    @GetMapping("/get_schedule-by-id")
    public SchedulingDTO getScheduleById(@RequestParam(required = true) String id){
        return adminServiceImpl.getDataSchedulingById(id);
    }

    @PutMapping("/update-scheduling")
    public ReqResUpdateScheduling updateScheduling(@RequestBody ReqResUpdateScheduling reqResUpdateScheduling){
        return adminServiceImpl.updateScheduling(reqResUpdateScheduling);
    }

    @DeleteMapping("/delete-scheduling")
    public ReqResDeleteScheduling deleteScheduling(@RequestBody ReqResDeleteScheduling reqResDeleteScheduling){
        return adminServiceImpl.deleteSchduling(reqResDeleteScheduling);
    }

    @GetMapping("/get-all-treatment")
    public List<TreatmentDTO> getAllTreatment(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        return adminServiceImpl.getAllTreatment(companyName, startDate, endDate);
    }

    @PostMapping("/upload-file")
    public ResponseSucces<String> uploadFile(
            @RequestParam("companyName") String companyName,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        String uploadFile = adminServiceImpl.uploadFile(companyName, multipartFile);
        return ResponseSucces.<String>builder().data(uploadFile).build();
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName){
        byte[] fileInBytes = adminServiceImpl.downloadFile(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("application/pdf")).body(fileInBytes);
    }

}
