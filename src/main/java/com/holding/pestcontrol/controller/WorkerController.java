package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.repository.SchedulingRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.repository.WorkerRepository;
import com.holding.pestcontrol.service.SpecificationSearch;
import com.holding.pestcontrol.service.worker.WorkerServiceImpl;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ReqResTreatment createTreatment(@RequestBody ReqResTreatment reqResTreatment){
        return workerService.createTreatment(reqResTreatment);
    }

}
