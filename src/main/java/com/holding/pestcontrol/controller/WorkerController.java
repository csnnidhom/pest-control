package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.service.worker.profile.WorkerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerServiceImpl workerService;
    @GetMapping
    public ReqResWorker get(){
        return workerService.get();
    }

    @PutMapping
    public ReqResWorker update(@RequestBody ReqResWorker request){
        return workerService.update(request);
    }

}
