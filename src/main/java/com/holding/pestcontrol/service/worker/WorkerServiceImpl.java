package com.holding.pestcontrol.service.worker;

import com.holding.pestcontrol.dto.entityDTO.*;
import com.holding.pestcontrol.dto.profileUser.ReqResWorker;
import com.holding.pestcontrol.dto.treatment.ReqResTreatmentCreate;
import com.holding.pestcontrol.dto.treatment.ReqResTreatmentUpdate;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ReqResTreatmentCreate createTreatment(ReqResTreatmentCreate reqResTreatmentCreate) {
        validationService.validate(reqResTreatmentCreate);

        Scheduling scheduling = schedulingRepository.findById(reqResTreatmentCreate.getIdSchedule())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id schedule not found"));

        if (!serviceTreatmenSlipRepository.existsByScheduling(scheduling) ){
            ServiceTreatmentSlip serviceTreatmentSlip = new ServiceTreatmentSlip();
            serviceTreatmentSlip.setId(UUID.randomUUID().toString());
            serviceTreatmentSlip.setScheduling(scheduling);
            serviceTreatmentSlip.setArea(reqResTreatmentCreate.getArea());

            //TreatmentType save
            TreatmentType treatmentType = new TreatmentType();
            treatmentType.setId(UUID.randomUUID().toString());
            treatmentType.setCf(reqResTreatmentCreate.getTreatmentTypeDTO().isCf());
            treatmentType.setHf(reqResTreatmentCreate.getTreatmentTypeDTO().isHf());
            treatmentType.setS(reqResTreatmentCreate.getTreatmentTypeDTO().isS());
            treatmentType.setB(reqResTreatmentCreate.getTreatmentTypeDTO().isB());
            treatmentType.setLv(reqResTreatmentCreate.getTreatmentTypeDTO().isLv());
            treatmentType.setT(reqResTreatmentCreate.getTreatmentTypeDTO().isT());
            treatmentType.setOt(reqResTreatmentCreate.getTreatmentTypeDTO().isOt());
            treatmentTypeRepository.save(treatmentType);
            //end TreatmentType

            serviceTreatmentSlip.setTreatmentType(treatmentType);
            serviceTreatmentSlip.setAi(Boolean.parseBoolean(reqResTreatmentCreate.getAi()));
            serviceTreatmentSlip.setRekarks(reqResTreatmentCreate.getRekarks());

            //ChemicalType
            Chemical chemical = new Chemical();
            chemical.setId(UUID.randomUUID().toString());
            chemical.setBahanAktif(reqResTreatmentCreate.getChemicalDTO().getBahanAktif());
            chemical.setDosis(reqResTreatmentCreate.getChemicalDTO().getDosis());
            chemical.setJumlah(reqResTreatmentCreate.getChemicalDTO().getJumlah());
            chemical.setKeterangan(reqResTreatmentCreate.getChemicalDTO().getKeterangan());
            chemicalRepository.save(chemical);
            //end ChemicalType

            serviceTreatmentSlip.setChemical(chemical);
            serviceTreatmentSlip.setDateWorking(reqResTreatmentCreate.getDate());
            serviceTreatmentSlip.setTimeIn(reqResTreatmentCreate.getTimeIn());
            serviceTreatmentSlip.setTimeOut(reqResTreatmentCreate.getTimeOut());
            serviceTreatmentSlip.setRekomendasiWorker(reqResTreatmentCreate.getRekomendasiWorker());
            serviceTreatmentSlip.setSaranClient(reqResTreatmentCreate.getSaranClient());
            serviceTreatmenSlipRepository.save(serviceTreatmentSlip);

            return ReqResTreatmentCreate.builder()
                    .message("Success add treatment")
                    .idTreatment(serviceTreatmentSlip.getId())
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data duplicate");
        }
    }

    @Override
    public List<TreatmentDTO> getAllServiceTreatment(String companyname, LocalDate startDate, LocalDate endDate) {
        Specification<ServiceTreatmentSlip> specification = SpecificationSearch.findAllServicetreatmentByWorkerAuthentication(companyname, startDate, endDate);
        List<ServiceTreatmentSlip> serviceTreatmentSlipList = serviceTreatmenSlipRepository.findAll(specification);
        return serviceTreatmentSlipList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReqResTreatmentUpdate updateTreatment(ReqResTreatmentUpdate reqResTreatmentUpdate) {
        validationService.validate(reqResTreatmentUpdate);

        ServiceTreatmentSlip serviceTreatmentSlip = serviceTreatmenSlipRepository.findById(reqResTreatmentUpdate.getIdTreatment())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Treatment not found"));

        TreatmentType treatmentType = treatmentTypeRepository.findById(reqResTreatmentUpdate.getTreatmentTypeDTO().getIdTreatmentType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Treatment Type not found"));

        Chemical chemical = chemicalRepository.findById(reqResTreatmentUpdate.getChemicalDTO().getIdChemical())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Chemical not found"));

        serviceTreatmentSlip.setArea(reqResTreatmentUpdate.getArea());
        //TreatmentType save
        treatmentType.setCf(reqResTreatmentUpdate.getTreatmentTypeDTO().isCf());
        treatmentType.setHf(reqResTreatmentUpdate.getTreatmentTypeDTO().isHf());
        treatmentType.setS(reqResTreatmentUpdate.getTreatmentTypeDTO().isS());
        treatmentType.setB(reqResTreatmentUpdate.getTreatmentTypeDTO().isB());
        treatmentType.setLv(reqResTreatmentUpdate.getTreatmentTypeDTO().isLv());
        treatmentType.setT(reqResTreatmentUpdate.getTreatmentTypeDTO().isT());
        treatmentType.setOt(reqResTreatmentUpdate.getTreatmentTypeDTO().isOt());
        treatmentTypeRepository.save(treatmentType);
        //end TreatmentType

        serviceTreatmentSlip.setAi(Boolean.parseBoolean(reqResTreatmentUpdate.getAi()));
        serviceTreatmentSlip.setRekarks(reqResTreatmentUpdate.getRekarks());
        //ChemicalType
        chemical.setBahanAktif(reqResTreatmentUpdate.getChemicalDTO().getBahanAktif());
        chemical.setDosis(reqResTreatmentUpdate.getChemicalDTO().getDosis());
        chemical.setJumlah(reqResTreatmentUpdate.getChemicalDTO().getJumlah());
        chemical.setKeterangan(reqResTreatmentUpdate.getChemicalDTO().getKeterangan());
        chemicalRepository.save(chemical);
        //end ChemicalType

        serviceTreatmentSlip.setDateWorking(reqResTreatmentUpdate.getDate());
        serviceTreatmentSlip.setTimeIn(reqResTreatmentUpdate.getTimeIn());
        serviceTreatmentSlip.setTimeOut(reqResTreatmentUpdate.getTimeOut());
        serviceTreatmentSlip.setRekomendasiWorker(reqResTreatmentUpdate.getRekomendasiWorker());
        serviceTreatmentSlip.setSaranClient(reqResTreatmentUpdate.getSaranClient());
        serviceTreatmenSlipRepository.save(serviceTreatmentSlip);

        return ReqResTreatmentUpdate.builder()
                .message("Success update treatment")
                .idTreatment(serviceTreatmentSlip.getId())
                .build();
    }

    private TreatmentDTO convertToDTO(ServiceTreatmentSlip serviceTreatmentSlip) {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setIdTreatment(serviceTreatmentSlip.getId());
        dto.setSchedulingDTO(convertScheduleToDTO(Collections.singletonList(serviceTreatmentSlip.getScheduling())));
        dto.setArea(serviceTreatmentSlip.getArea());
        dto.setTreatmentTypeDTO(convertTreatmentTypeToDTO(Collections.singletonList(serviceTreatmentSlip.getTreatmentType())));
        dto.setAi(String.valueOf(serviceTreatmentSlip.isAi()));
        dto.setRekarks(serviceTreatmentSlip.getRekarks());
        dto.setChemicalDTO(convertChemicalToDTO(Collections.singletonList(serviceTreatmentSlip.getChemical())));
        dto.setDate(serviceTreatmentSlip.getDateWorking());
        dto.setTimeIn(serviceTreatmentSlip.getTimeIn());
        dto.setTimeOut(serviceTreatmentSlip.getTimeOut());
        dto.setRekomendasiWorker(serviceTreatmentSlip.getRekomendasiWorker());
        dto.setSaranClient(serviceTreatmentSlip.getSaranClient());
        return dto;
    }

    private List<ChemicalDTO> convertChemicalToDTO(List<Chemical> chemical) {
        return chemical.stream()
                .map(ToChemicalDTO -> {
                    ChemicalDTO chemicalDTO = new ChemicalDTO();
                    chemicalDTO.setIdChemical(ToChemicalDTO.getId());
                    chemicalDTO.setBahanAktif(ToChemicalDTO.getBahanAktif());
                    chemicalDTO.setDosis(ToChemicalDTO.getDosis());
                    chemicalDTO.setJumlah(ToChemicalDTO.getJumlah());
                    chemicalDTO.setKeterangan(ToChemicalDTO.getKeterangan());
                    return chemicalDTO;
                }).collect(Collectors.toList());
    }

    private List<TreatmentTypeDTO> convertTreatmentTypeToDTO(List<TreatmentType> treatmentType) {
        return treatmentType.stream()
                .map(ToTreatmentDTO -> {
                    TreatmentTypeDTO treatmentTypeDTO = new TreatmentTypeDTO();
                    treatmentTypeDTO.setIdTreatmentType(ToTreatmentDTO.getId());
                    treatmentTypeDTO.setCf(ToTreatmentDTO.isCf());
                    treatmentTypeDTO.setHf(ToTreatmentDTO.isHf());
                    treatmentTypeDTO.setS(ToTreatmentDTO.isS());
                    treatmentTypeDTO.setB(ToTreatmentDTO.isB());
                    treatmentTypeDTO.setLv(ToTreatmentDTO.isLv());
                    treatmentTypeDTO.setT(ToTreatmentDTO.isT());
                    treatmentTypeDTO.setOt(ToTreatmentDTO.isOt());
                    return treatmentTypeDTO;
                }).collect(Collectors.toList());
    }

    private List<SchedulingDTO> convertScheduleToDTO(List<Scheduling> scheduling) {
        return scheduling.stream()
                .map(ToSchedulingDTO -> {
                    SchedulingDTO schedulingDTO = new SchedulingDTO();
                    schedulingDTO.setIdSchedule(ToSchedulingDTO.getId());
                    schedulingDTO.setClient(convertClientToDTO(Collections.singletonList(ToSchedulingDTO.getClient())));
                    schedulingDTO.setWorker(convertWorkerToDTO(Collections.singletonList(ToSchedulingDTO.getWorker())));
                    return schedulingDTO;
                }).collect(Collectors.toList());
    }

    private List<WorkerDTO> convertWorkerToDTO(List<Worker> worker) {
        return worker.stream()
                .map(ToWorkerDTO -> {
                    WorkerDTO workerDTO = new WorkerDTO();
                    workerDTO.setIdWorker(ToWorkerDTO.getId());
                    workerDTO.setNamaLengkap(ToWorkerDTO.getNamaLengkap());
                    workerDTO.setAlamat(ToWorkerDTO.getAlamat());
                    workerDTO.setNoTelp(ToWorkerDTO.getNoTelp());
                    return workerDTO;
                }).collect(Collectors.toList());
    }

    private List<ClientDTO> convertClientToDTO(List<Client> clients) {
        return clients.stream()
                .map(ToClientDTO ->{
                    ClientDTO clientDTO = new ClientDTO();
                    clientDTO.setIdClient(ToClientDTO.getId());
                    clientDTO.setNamaPerusahaan(ToClientDTO.getNamaPerusahaan());
                    return clientDTO;
                }).collect(Collectors.toList());
    }

}
