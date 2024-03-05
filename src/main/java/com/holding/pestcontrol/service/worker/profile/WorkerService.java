package com.holding.pestcontrol.service.worker.profile;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.dto.ReqResWorker;
import org.springframework.http.ResponseEntity;

public interface WorkerService {
    ReqResWorker getDetailProfile();
    ReqResWorker updateDetailProfile(ReqResWorker request);

}
