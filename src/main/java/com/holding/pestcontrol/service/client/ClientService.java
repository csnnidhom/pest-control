package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.ReqResClient;

public interface ClientService {

    ReqResClient get(ReqResClient request);
    ReqResClient update(ReqResClient request);
}
