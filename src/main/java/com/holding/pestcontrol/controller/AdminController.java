package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminCreateDetailUser;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminGetDelete;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminUpdateDetailUser;
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
import org.springframework.web.bind.annotation.*;

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
        return adminServiceImpl.create(request);
    }

    @PutMapping("/update-detail-user")
    public ReqResAdminUpdateDetailUser update(@RequestBody ReqResAdminUpdateDetailUser request){
        return adminServiceImpl.update(request);
    }

    @GetMapping("/search-client")
    public List<Client> searchClient(
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
    public ReqResAdminGetDelete get(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.getDetailByEmail(reqResAdminGetDelete);
    }

    @PatchMapping("/switch-status")
    public ReqResAdminGetDelete switchStatus(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.switchStatus(reqResAdminGetDelete);
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
}
