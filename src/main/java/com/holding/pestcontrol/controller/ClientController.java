package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @GetMapping
    public ReqResClient get(@RequestBody ReqResClient reqResClient){
        return clientServiceImpl.get(reqResClient);
    }

    @PutMapping
    public ReqResClient update(@RequestBody ReqResClient request){
        return clientServiceImpl.update(request);
    }

}
