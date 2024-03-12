package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.dto.ResponseSucces;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @GetMapping("/get-detail-profile")
    public ReqResClient getDetailProfile(){
        return clientServiceImpl.getDetailProfile();
    }

    @PutMapping("/update-detail-profile")
    public ReqResClient updateDetailProfile(@RequestBody ReqResClient request){
        return clientServiceImpl.updateDetailProfile(request);
    }

    @GetMapping("/get-all-schedule-client")
    public List<Scheduling> getAllSchedule(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        return clientServiceImpl.getAllScheduleClient(startDate, endDate);
    }

}
