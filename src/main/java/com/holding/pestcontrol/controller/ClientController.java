package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.dto.ResponseSucces;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @GetMapping("/get-detail-profile")
    public ReqResClient getDetailProfile(){
        return clientServiceImpl.getDetailProfile();
    }

    @PutMapping("/update-detail-profile")
    public ReqResClient updateDetailProfile(@RequestBody ReqResClient request){
        return clientServiceImpl.updateDetailProfile(request);
    }

}
