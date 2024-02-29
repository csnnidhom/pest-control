package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResWorker;
import org.springframework.http.ResponseEntity;

public interface WorkerService {
    ReqResWorker get(ReqResWorker reqResWorker);
    ReqResWorker update(ReqResWorker request);
}
