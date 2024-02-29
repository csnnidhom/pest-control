package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.ReqResClient;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.entity.Worker;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.repository.WorkerRepository;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService{

    private final ValidationService validationService;

    private final UserRepository userRepository;

    private final WorkerRepository workerRepository;

    @Override
    public ReqResWorker get(ReqResWorker reqResWorker) {
        validationService.validate(reqResWorker);

        User user = userRepository.findByEmail(reqResWorker.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        Worker worker = (Worker) workerRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Not Found "));

        return ReqResWorker.builder()
                .id(worker.getId())
                .namaLengkap(worker.getNamaLengkap())
                .alamat(worker.getAlamat())
                .noTelp(worker.getNoTelp())
                .build();
    }

    @Override
    public ReqResWorker update(ReqResWorker request) {
        validationService.validate(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        Worker worker = (Worker) workerRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Detail Client Not Found "));

        worker.setUser(user);
        worker.setNamaLengkap(request.getNamaLengkap());
        worker.setNoTelp(request.getNoTelp());
        workerRepository.save(worker);

        return ReqResWorker.builder()
                .message("Update Success")
                .id(worker.getId())
                .namaLengkap(worker.getNamaLengkap())
                .alamat(worker.getAlamat())
                .noTelp(worker.getNoTelp())
                .build();
    }
}
