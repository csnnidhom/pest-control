package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.dto.ReqResWorker;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.repository.*;
import com.holding.pestcontrol.service.SpecificationSearch;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerServiceImpl implements WorkerService{

    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final SchedulingRepository schedulingRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;
    private final ChemicalRepository chemicalRepository;
    private final ServiceTreatmenSlipRepository serviceTreatmenSlipRepository;

    @Override
    public ReqResWorker getDetailProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Worker worker = (Worker) workerRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Worker Not Found "));

        return ReqResWorker.builder()
                .detailWorker(worker)
                .build();
    }

    @Override
    public ReqResWorker updateDetailProfile(ReqResWorker request) {
        validationService.validate(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
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

    @Override
    public List<Scheduling> getAllScheduleWorker(String companyName, LocalDate startDate, LocalDate endDate) {
        Specification<Scheduling> specification = SpecificationSearch.findAllScheduleByWorkerAuthentication(companyName, startDate, endDate);
        return schedulingRepository.findAll(specification);
    }

    @Override
    public ReqResTreatment createTreatment(ReqResTreatment reqResTreatment) {
        validationService.validate(reqResTreatment);

        Scheduling scheduling = schedulingRepository.findById(reqResTreatment.getIdSchedule())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id schedule not found"));

        if (!serviceTreatmenSlipRepository.existsByScheduling(scheduling) ){
            ServiceTreatmentSlip serviceTreatmentSlip = new ServiceTreatmentSlip();
            serviceTreatmentSlip.setId(UUID.randomUUID().toString());
            serviceTreatmentSlip.setScheduling(scheduling);
            serviceTreatmentSlip.setArea(reqResTreatment.getArea());

            //TreatmentType save
            TreatmentType treatmentType = new TreatmentType();
            treatmentType.setId(UUID.randomUUID().toString());
            treatmentType.setCf(reqResTreatment.getRequestTreatmentType().isCf());
            treatmentType.setHf(reqResTreatment.getRequestTreatmentType().isHf());
            treatmentType.setS(reqResTreatment.getRequestTreatmentType().isS());
            treatmentType.setB(reqResTreatment.getRequestTreatmentType().isB());
            treatmentType.setLv(reqResTreatment.getRequestTreatmentType().isLv());
            treatmentType.setT(reqResTreatment.getRequestTreatmentType().isT());
            treatmentType.setOt(reqResTreatment.getRequestTreatmentType().isOt());
            treatmentTypeRepository.save(treatmentType);
            //end TreatmentType

            serviceTreatmentSlip.setTreatmentType(treatmentType);
            serviceTreatmentSlip.setAi(Boolean.parseBoolean(reqResTreatment.getAi()));
            serviceTreatmentSlip.setRekarks(reqResTreatment.getRekarks());

            //ChemicalType
            Chemical chemical = new Chemical();
            chemical.setId(UUID.randomUUID().toString());
            chemical.setBahanAktif(reqResTreatment.getRequestChemicalchemical().getBahanAktif());
            chemical.setDosis(reqResTreatment.getRequestChemicalchemical().getDosis());
            chemical.setJumlah(reqResTreatment.getRequestChemicalchemical().getJumlah());
            chemical.setKeterangan(reqResTreatment.getRequestChemicalchemical().getKeterangan());
            chemicalRepository.save(chemical);
            //end ChemicalType

            serviceTreatmentSlip.setChemical(chemical);
            serviceTreatmentSlip.setDateWorking(reqResTreatment.getDate());
            serviceTreatmentSlip.setTimeIn(reqResTreatment.getTimeIn());
            serviceTreatmentSlip.setTimeOut(reqResTreatment.getTimeOut());
            serviceTreatmentSlip.setRekomendasiWorker(reqResTreatment.getRekomendasiWorker());
            serviceTreatmentSlip.setSaranClient(reqResTreatment.getSaranClient());
            serviceTreatmenSlipRepository.save(serviceTreatmentSlip);

            return ReqResTreatment.builder()
                    .message("Success add treatment")
                    .idTreatment(serviceTreatmentSlip.getId())
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data duplicate");
        }
    }

    @Override
    public List<ServiceTreatmentSlip> getAllServiceTreatment(String companyname, LocalDate startDate, LocalDate endDate) {
        Specification<ServiceTreatmentSlip> specification = SpecificationSearch.findAllServicetreatmentByWorkerAuthentication(companyname, startDate, endDate);
        return serviceTreatmenSlipRepository.findAll(specification);
    }

}
