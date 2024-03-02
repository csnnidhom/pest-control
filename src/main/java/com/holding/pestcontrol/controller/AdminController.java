package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResAdminCreateUpdate;
import com.holding.pestcontrol.dto.ReqResAdminGetDelete;
import com.holding.pestcontrol.entity.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.service.admin.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    @PostMapping
    public ReqResAdminCreateUpdate create(@RequestBody ReqResAdminCreateUpdate request){
        return adminServiceImpl.create(request);
    }

    @GetMapping("/get-detail-by-email")
    public ReqResAdminGetDelete get(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.getDetailByEmail(reqResAdminGetDelete);
    }

    @PutMapping
    public ReqResAdminCreateUpdate update(@RequestBody ReqResAdminCreateUpdate request){
        return adminServiceImpl.update(request);
    }

    @GetMapping
    public List<User> getAllByRole(@RequestParam(name = "role") Role role){
        return adminServiceImpl.getAllDataByRole(role);
    }

    @PatchMapping
    public ReqResAdminGetDelete switchStatus(@RequestBody ReqResAdminGetDelete reqResAdminGetDelete){
        return adminServiceImpl.switchStatus(reqResAdminGetDelete);
    }


}
