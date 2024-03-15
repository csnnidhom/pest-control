package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.dto.profileUser.ReqResClient;
import com.holding.pestcontrol.dto.response.ResponseSucces;
import com.holding.pestcontrol.dto.treatment.ReqResChangeStatusTreatment;
import com.holding.pestcontrol.dto.treatment.ReqResSuggestionTreatment;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping("/get-all-service-treatment-client")
    public List<TreatmentDTO> getAllTreatment(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        return clientServiceImpl.getAllServiceTreatment(startDate, endDate);
    }

    @PatchMapping("/change-status-complete")
    public ReqResChangeStatusTreatment changeStatus(@RequestBody ReqResChangeStatusTreatment reqResChangeStatusTreatment){
        return clientServiceImpl.changeStatusTreatment(reqResChangeStatusTreatment);
    }

    @PatchMapping("/create-suggestion")
    public ReqResSuggestionTreatment createSuggestion(@RequestBody ReqResSuggestionTreatment reqResSuggestionTreatment){
        return clientServiceImpl.createSuggestion(reqResSuggestionTreatment);
    }
}
