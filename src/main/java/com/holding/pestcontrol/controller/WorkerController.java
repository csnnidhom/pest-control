package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.service.worker.profile.WorkerServiceImpl;
import com.holding.pestcontrol.service.worker.schedule.ScheduleImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerServiceImpl workerService;

    private final ScheduleImpl treatmentService;

    private final ScheduleImpl scheduleService;

    @GetMapping("/get-detail-profile")
    public ReqResWorker getDetailProfile(){
        return workerService.getDetailProfile();
    }

    @PutMapping("/update-detail-profile")
    public ReqResWorker updateDetailProfile(@RequestBody ReqResWorker request){
        return workerService.updateDetailProfile(request);
    }

    @GetMapping("/get-schedule")
    public List<Scheduling> getSchedule(){
        return treatmentService.getAllSchedule();
    }

    @GetMapping("/get-schedule-by-client")
    public List<Scheduling> getScheduleByClient(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
        )
    {
        return scheduleService.searchSchedule(companyName, startDate, endDate);
    }

}
