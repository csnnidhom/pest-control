package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.service.client.ClientServiceImpl;
import com.holding.pestcontrol.service.worker.WorkerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerServiceImpl workerService;
    @GetMapping
    public ReqResWorker get(@RequestBody ReqResWorker reqResWorker){
        return workerService.get(reqResWorker);
    }

    @PutMapping
    public ReqResWorker update(@RequestBody ReqResWorker request){
        return workerService.update(request);
    }

}
