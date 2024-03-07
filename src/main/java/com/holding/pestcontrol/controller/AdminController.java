package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.enumm.WorkingType;
import com.holding.pestcontrol.service.admin.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/detail-client")
    public ReqResAdminCreateUpdate create(@RequestBody ReqResAdminCreateUpdate request){
        return adminServiceImpl.create(request);
    }

    @GetMapping("/get-detail-by-email")
    public ReqResAdminGetDelete get(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.getDetailByEmail(reqResAdminGetDelete);
    }

    @PutMapping("/update-detail")
    public ReqResAdminCreateUpdate update(@RequestBody ReqResAdminCreateUpdate request){
        return adminServiceImpl.update(request);
    }

    @GetMapping("/get-all-by-role")
    public List<User> getAllByRole(@RequestParam(name = "role") Role role){
        return adminServiceImpl.getAllDataByRole(role);
    }

    @PatchMapping("/switch-status")
    public ReqResAdminGetDelete switchStatus(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.switchStatus(reqResAdminGetDelete);
    }

    @PostMapping("/create-scheduling")
    public ReqResCreateScheduling create(@RequestBody ReqResCreateScheduling request){
        return adminServiceImpl.createScheduling(request);
    }

    @GetMapping("/get-all-scheduling")
    public List<Scheduling> getAllSceduling(){
        return adminServiceImpl.getAllScheduling();
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
    public List<TreatmentDTO> getAllTreatment(){
        return adminServiceImpl.getAllTreatment();
    }
}
