package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResEditPassword;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.SchedulingRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.service.SpecificationSearch;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ValidationService validationService;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final SchedulingRepository schedulingRepository;

    @Override
    public ReqResClient getDetailProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Client Not Found "));

        return ReqResClient.builder()
                .detailClient(client)
                .build();

    }

    @Override
    public ReqResClient updateDetailProfile(ReqResClient request) {
        validationService.validate(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
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

    @Override
    public List<Scheduling> getAllScheduleClient(LocalDate startDate, LocalDate endDate) {

        LocalDate startDateNow = LocalDate.now();
        LocalDate endDateNow = LocalDate.now();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Client Not Found "));

        if (startDate == null || endDate == null){
            return schedulingRepository.findAllByClientAndDateWorkingBetween(client,startDateNow, endDateNow);
        }else {
            return schedulingRepository.findAllByClientAndDateWorkingBetween(client,startDate, startDate);
        }

    }
}

