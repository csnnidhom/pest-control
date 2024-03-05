package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.enumm.WorkingType;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.SchedulingRepository;
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

    private final SchedulingRepository schedulingRepository;

    @Override
    public Role[] getRole() {
        return Role.values();
    }

    @Override
    public WorkingType[] getWorkingType() {
        return WorkingType.values();
    }

    @Override
    public FreqType[] getFreqType() {
        return FreqType.values();
    }

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

        String message;
        if (!user.isStatus()){
            user.setStatus(true);
            message = "The account is ACTIVE";
        } else {
            user.setStatus(false);
            message = "The account is NOT ACTIVE";
        }

        userRepository.save(user);

        return ReqResAdminGetDelete.builder()
                .message(message)
                .build();
    }

    @Override
    public ReqResCreateScheduling createScheduling(ReqResCreateScheduling reqResCreateScheduling) {
        validationService.validate(reqResCreateScheduling);

        Client client = (Client) clientRepository.findByNamaPerusahaan(reqResCreateScheduling.getCompanyName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company name not found"));

        Worker worker = (Worker) workerRepository.findByNamaLengkap(reqResCreateScheduling.getWorkerName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker not found"));

        Scheduling scheduling = new Scheduling();
        scheduling.setId(UUID.randomUUID().toString());
        scheduling.setClient(client);
        scheduling.setWorker(worker);
        scheduling.setStartContractPeriod(reqResCreateScheduling.getData().getStartContractPeriod());
        scheduling.setEndContractPeriod(reqResCreateScheduling.getData().getEndContractPeriod());
        scheduling.setWorkingType(WorkingType.valueOf(reqResCreateScheduling.getData().getWorkingType()));
        scheduling.setFreq(reqResCreateScheduling.getData().getFreq());
        scheduling.setFreqType(FreqType.valueOf(reqResCreateScheduling.getData().getFreqType()));
        scheduling.setTimeStart(reqResCreateScheduling.getData().getTimeStart());
        scheduling.setTimeEnd(reqResCreateScheduling.getData().getTimeEnd());
        scheduling.setPic(reqResCreateScheduling.getData().getPic());
        scheduling.setNoTelpPic(reqResCreateScheduling.getData().getNoTelpPic());
        scheduling.setDateWorking(reqResCreateScheduling.getData().getDateWorking());
        schedulingRepository.save(scheduling);

        return ReqResCreateScheduling.builder()
                .statusCode(200)
                .message("Success add Scheduling")
                .data(ReqResSchedulingData.builder()
                        .id(scheduling.getId())
                        .companyName(reqResCreateScheduling.getCompanyName())
                        .workerName(reqResCreateScheduling.getWorkerName())
                        .startContractPeriod(scheduling.getStartContractPeriod())
                        .endContractPeriod(scheduling.getEndContractPeriod())
                        .workingType(String.valueOf(scheduling.getWorkingType()))
                        .freq(scheduling.getFreq())
                        .freqType(String.valueOf(scheduling.getFreqType()))
                        .timeStart(scheduling.getTimeStart())
                        .timeEnd(scheduling.getTimeEnd())
                        .pic(scheduling.getPic())
                        .noTelpPic(scheduling.getNoTelpPic())
                        .dateWorking(scheduling.getDateWorking())
                        .build())
                .build();
    }

    @Override
    public List<Scheduling> getAllScheduling() {
        return schedulingRepository.findAll();
    }

    @Override
    public ReqResUpdateScheduling updateScheduling(ReqResUpdateScheduling reqResUpdateScheduling) {
        validationService.validate(reqResUpdateScheduling);

        Client client = (Client) clientRepository.findByNamaPerusahaan(reqResUpdateScheduling.getCompanyName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company name not found"));

        Worker worker = (Worker) workerRepository.findByNamaLengkap(reqResUpdateScheduling.getWorkerName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker not found"));

        Scheduling scheduling = schedulingRepository.findById(reqResUpdateScheduling.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Scheduling not found"));

        scheduling.setClient(client);
        scheduling.setWorker(worker);
        scheduling.setStartContractPeriod(reqResUpdateScheduling.getData().getStartContractPeriod());
        scheduling.setEndContractPeriod(reqResUpdateScheduling.getData().getEndContractPeriod());
        scheduling.setWorkingType(WorkingType.valueOf(reqResUpdateScheduling.getData().getWorkingType()));
        scheduling.setFreq(reqResUpdateScheduling.getData().getFreq());
        scheduling.setFreqType(FreqType.valueOf(reqResUpdateScheduling.getData().getFreqType()));
        scheduling.setTimeStart(reqResUpdateScheduling.getData().getTimeStart());
        scheduling.setTimeEnd(reqResUpdateScheduling.getData().getTimeEnd());
        scheduling.setPic(reqResUpdateScheduling.getData().getPic());
        scheduling.setNoTelpPic(reqResUpdateScheduling.getData().getNoTelpPic());
        scheduling.setDateWorking(reqResUpdateScheduling.getData().getDateWorking());
        schedulingRepository.save(scheduling);

        return ReqResUpdateScheduling.builder()
                .statusCode(200)
                .message("Success update Scheduling")
                .data(ReqResSchedulingData.builder()
                        .id(scheduling.getId())
                        .companyName(reqResUpdateScheduling.getCompanyName())
                        .workerName(reqResUpdateScheduling.getWorkerName())
                        .startContractPeriod(scheduling.getStartContractPeriod())
                        .endContractPeriod(scheduling.getEndContractPeriod())
                        .workingType(String.valueOf(scheduling.getWorkingType()))
                        .freq(scheduling.getFreq())
                        .freqType(String.valueOf(scheduling.getFreqType()))
                        .timeStart(scheduling.getTimeStart())
                        .timeEnd(scheduling.getTimeEnd())
                        .pic(scheduling.getPic())
                        .noTelpPic(scheduling.getNoTelpPic())
                        .dateWorking(scheduling.getDateWorking())
                        .build())
                .build();
    }

    @Override
    public ReqResDeleteScheduling deleteSchduling(ReqResDeleteScheduling reqResDeleteScheduling) {
        validationService.validate(reqResDeleteScheduling);

        Scheduling scheduling = schedulingRepository.findById(reqResDeleteScheduling.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Scheduling not found"));

        schedulingRepository.delete(scheduling);

        return ReqResDeleteScheduling.builder()
                .message("Success delete schedule")
                .id(reqResDeleteScheduling.getId())
                .build();

    }

}
