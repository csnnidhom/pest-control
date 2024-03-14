package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.treatment.ReqResTreatmentCreate;
import com.holding.pestcontrol.dto.treatment.ReqResTreatmentUpdate;
import com.holding.pestcontrol.dto.profileUser.ReqResWorker;
import com.holding.pestcontrol.dto.entityDTO.TreatmentDTO;
import com.holding.pestcontrol.entity.Chemical;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.TreatmentType;
import com.holding.pestcontrol.repository.SchedulingRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.repository.WorkerRepository;
import com.holding.pestcontrol.service.worker.WorkerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final WorkerServiceImpl workerService;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final SchedulingRepository schedulingRepository;

    @GetMapping("/get-detail-profile")
    public ReqResWorker getDetailProfile(){
        return workerService.getDetailProfile();
    }

    @PutMapping("/update-detail-profile")
    public ReqResWorker updateDetailProfile(@RequestBody ReqResWorker request){
        return workerService.updateDetailProfile(request);
    }

    @GetMapping("/get-all-schedule-worker")
    public List<Scheduling> getAllSchedule(
            @RequestParam (required = false) String companyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        return workerService.getAllScheduleWorker(companyName, startDate, endDate);
    }

    @PostMapping("/create-treatment")
    public ReqResTreatmentCreate createTreatment(@RequestBody ReqResTreatmentCreate reqResTreatmentCreate){
        return workerService.createTreatment(reqResTreatmentCreate);
    }

    @PutMapping("/update-treatment")
    public ReqResTreatmentUpdate updateTreatment(@RequestBody ReqResTreatmentUpdate reqResTreatmentUpdate){
        return workerService.updateTreatment(reqResTreatmentUpdate);
    }

    @GetMapping("/get-all-service-treatment-worker")
    public List<TreatmentDTO> getAllTreatment(
            @RequestParam (required = false) String companyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        return workerService.getAllServiceTreatment(companyName, startDate, endDate);
    }

}
