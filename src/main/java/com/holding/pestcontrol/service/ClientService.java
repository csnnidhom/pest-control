package com.holding.pestcontrol.service;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientService {

    private final ValidationService validationService;

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    public ReqResClient create(ReqResClient request){
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        Client client = new Client();
        client.setUser(user);
        client.setNamaPerusahaan(request.getNamaPerusahaan());
        client.setAlamat(request.getAlamat());
        client.setEmail(request.getEmail());
        client.setNoTelp(request.getNoTelp());

        clientRepository.save(client);

        return ReqResClient.builder()
                .message("Insert Success")
                .namaPerusahaan(client.getNamaPerusahaan())
                .alamat(client.getAlamat())
                .email(client.getEmail())
                .noTelp(client.getNoTelp())
                .build();
    }

    public ReqResClient update(ReqResClient request){
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        Client client = clientRepository.findByUserAndId(user, Integer.valueOf(request.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Detail Client Not Found "));

        client.setUser(user);
        client.setNamaPerusahaan(request.getNamaPerusahaan());
        client.setAlamat(request.getAlamat());
        client.setEmail(request.getEmail());
        client.setNoTelp(request.getNoTelp());
        clientRepository.save(client);

        return ReqResClient.builder()
                .message("Update Success")
                .namaPerusahaan(client.getNamaPerusahaan())
                .alamat(client.getAlamat())
                .email(client.getEmail())
                .noTelp(client.getNoTelp())
                .build();

    }

//    public ReqResClient get(Client client, Integer id){
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));
//        Client client = clientRepository.findFirstByUserAndId(user, id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Detail Client Not Found "));
//        return ReqResClient.builder()
//                .namaPerusahaan(client.getNamaPerusahaan())
//                .alamat(client.getAlamat())
//                .email(client.getEmail())
//                .noTelp(client.getNoTelp())
//                .build();
//    }

}
