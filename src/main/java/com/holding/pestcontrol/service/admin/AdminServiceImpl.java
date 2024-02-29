package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.ReqResAdminCreateUpdate;
import com.holding.pestcontrol.dto.ReqResAdminGetDelete;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.repository.WorkerRepository;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{

    private final ValidationService validationService;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final WorkerRepository workerRepository;

    @Override
    public ReqResAdminCreateUpdate create(ReqResAdminCreateUpdate reqResAdminCreateUpdate) {
        validationService.validate(reqResAdminCreateUpdate);

        ReqResAdminCreateUpdate response = new ReqResAdminCreateUpdate();

        User user = userRepository.findByEmail(reqResAdminCreateUpdate.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        String role = user.getRole().name();

        if (role.equals("CLIENT")){
            Client client = new Client();
            client.setId(UUID.randomUUID().toString());
            client.setUser(user);
            client.setNamaPerusahaan(reqResAdminCreateUpdate.getNamaPerusahaan());
            client.setAlamat(reqResAdminCreateUpdate.getAlamat());
            client.setNoTelp(reqResAdminCreateUpdate.getNoTelp());
            clientRepository.save(client);
            response.setMessage("Success insert detail client");
            response.setId(client.getId());

        } else if (role.equals("WORKER")) {
            Worker worker = new Worker();
            worker.setId(UUID.randomUUID().toString());
            worker.setUser(user);
            worker.setNamaLengkap(reqResAdminCreateUpdate.getNamaLengkap());
            worker.setAlamat(reqResAdminCreateUpdate.getAlamat());
            worker.setNoTelp(reqResAdminCreateUpdate.getNoTelp());
            workerRepository.save(worker);
            response.setMessage("Success insert detail worker");
            response.setId(worker.getId());
        } else {
            response.setMessage("You are ADMIN");
        }
        return response;
    }

    @Override
    public ReqResAdminCreateUpdate update(ReqResAdminCreateUpdate reqResAdminCreateUpdate) {
        validationService.validate(reqResAdminCreateUpdate);

        ReqResAdminCreateUpdate response = new ReqResAdminCreateUpdate();

        User user = userRepository.findByEmail(reqResAdminCreateUpdate.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));


        String role = user.getRole().name();

        if (role.equals("CLIENT")){
            Client client = (Client) clientRepository.findByUserAndId(user, reqResAdminCreateUpdate.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

            client.setNamaPerusahaan(reqResAdminCreateUpdate.getNamaPerusahaan());
            client.setAlamat(reqResAdminCreateUpdate.getAlamat());
            client.setNoTelp(reqResAdminCreateUpdate.getNoTelp());
            clientRepository.save(client);
            response.setMessage("Success update detail client");
            response.setId(client.getId());

        } else if (role.equals("WORKER")) {
            Worker worker = (Worker) workerRepository.findByUserAndId(user, reqResAdminCreateUpdate.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

            worker.setNamaLengkap(reqResAdminCreateUpdate.getNamaLengkap());
            worker.setNoTelp(reqResAdminCreateUpdate.getNoTelp());
            workerRepository.save(worker);
            response.setMessage("Success update detail worker");
            response.setId(worker.getId());
        }
        return response;
    }

    @Override
    public ReqResAdminGetDelete getDetailByEmail(ReqResAdminGetDelete reqResAdminGetDelete) {
        validationService.validate(reqResAdminGetDelete);

        User user = userRepository.findByEmail(reqResAdminGetDelete.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        String role = user.getRole().name();

        Client client = null;
        Worker worker = null;
        String forAdmin = null;

        if (role.equals("CLIENT")) {
            client = (Client) clientRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail client not found"));
        } else if (role.equals("WORKER")) {
            worker = (Worker) workerRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail worker not found"));
        } else {
            forAdmin = "You are ADMIN";
        }

        return ReqResAdminGetDelete.builder()
                .message(forAdmin)
                .detailClient(client)
                .workerDetail(worker)
                .build();
    }

    @Override
    public List<User> getAllDataByRole(Role role) {
        validationService.validate(role);

        return userRepository.findByRole(role);
    }

    @Override
    public ReqResAdminGetDelete switchStatus(ReqResAdminGetDelete reqResAdminGetDelete) {
        validationService.validate(reqResAdminGetDelete);

        User user = userRepository.findByEmail(reqResAdminGetDelete.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (!user.isStatus()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The account is not active");
        }

        user.setStatus(false);
        userRepository.save(user);

        return ReqResAdminGetDelete.builder()
                .message("Success, The account is not active")
                .build();
    }

}
