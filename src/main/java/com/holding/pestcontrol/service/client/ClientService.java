package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;

public interface ClientService {
    ReqResClient get();
    ReqResClient update(ReqResClient request);

    ReqResEditPassword editPassword(ReqResEditPassword reqResEditPassword);
}
