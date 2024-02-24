package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ResponseSucces;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    @PostMapping("/admin/{username}")
    public ResponseSucces<ReqResClient> create(
            @RequestBody ReqResClient request,
            @PathVariable String username
    ){
        request.setUsername(username);
        ReqResClient response = clientService.create( request);
        return ResponseSucces.<ReqResClient>builder().data(response).build();
    }

    @PutMapping("/admin/{username}/{idDetailClient}")
    public ResponseSucces<ReqResClient> update(
            @RequestBody ReqResClient request,
            @PathVariable String username,
            @PathVariable Integer idDetailClient
    ){

        request.setUsername(username);
        request.setId(idDetailClient);
        ReqResClient response = clientService.update(request);
        return ResponseSucces.<ReqResClient>builder().data(response).build();
    }

//    @GetMapping("/admin/{id}")
//    public ResponseSucces<ReqResClient> get(
//            Client client,
//            @PathVariable(name = "id") Integer id
//    ) {
//        ReqResClient reqResClient = clientService.get(client, id);
//        return ResponseSucces.<ReqResClient>builder().data(reqResClient).build();
//    }

}
