package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.service.worker.profile.WorkerServiceImpl;
import com.holding.pestcontrol.service.worker.treatment.TreatmentImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerServiceImpl workerService;

    private final TreatmentImpl treatmentService;
    @GetMapping("/get-detail-profile")
    public ReqResWorker getDetailProfile(){
        return workerService.getDetailProfile();
    }

    @PutMapping("/update-detail-profile")
    public ReqResWorker updateDetailProfile(@RequestBody ReqResWorker request){
        return workerService.updateDetailProfile(request);
    }

    @GetMapping("/get-schedul")
    public List<Scheduling> getSchedule(){
        return treatmentService.getAllSchedule();
    }

}
