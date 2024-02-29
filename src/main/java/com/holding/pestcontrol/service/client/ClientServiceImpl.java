package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.service.ValidationService;
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
public class ClientServiceImpl implements ClientService {

    private final ValidationService validationService;

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    @Override
    public ReqResClient get(ReqResClient request) {
        validationService.validate(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Client Not Found "));

        return ReqResClient.builder()
                .id(client.getId())
                .namaPerusahaan(client.getNamaPerusahaan())
                .alamat(client.getAlamat())
                .noTelp(client.getNoTelp())
                .build();
    }

    @Override
    public ReqResClient update(ReqResClient request) {
        validationService.validate(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Detail Client Not Found "));

        client.setUser(user);
        client.setNamaPerusahaan(request.getNamaPerusahaan());
        client.setAlamat(request.getAlamat());
        client.setNoTelp(request.getNoTelp());
        clientRepository.save(client);

        return ReqResClient.builder()
                .message("Update Success")
                .id(client.getId())
                .namaPerusahaan(client.getNamaPerusahaan())
                .alamat(client.getAlamat())
                .noTelp(client.getNoTelp())
                .build();
    }
}

