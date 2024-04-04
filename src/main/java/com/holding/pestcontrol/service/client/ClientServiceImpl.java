package com.holding.pestcontrol.service.client;

import com.holding.pestcontrol.dto.entityDTO.*;
import com.holding.pestcontrol.dto.profileUser.ReqResClient;
import com.holding.pestcontrol.dto.treatment.ReqResChangeStatusTreatment;
import com.holding.pestcontrol.dto.treatment.ReqResSuggestionTreatment;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.repository.*;
import com.holding.pestcontrol.service.SpecificationSearch;
import com.holding.pestcontrol.service.ValidationService;
import com.holding.pestcontrol.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ValidationService validationService;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final SchedulingRepository schedulingRepository;
    private final ServiceTreatmenSlipRepository serviceTreatmenSlipRepository;
    private final UploadFileRepository uploadFileRepository;

    @Override
    public ReqResClient getDetailProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Client Not Found "));

        UploadFile uploadFile = uploadFileRepository.findByClient(client)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        return ReqResClient.builder()
                .detailClient(client)
                .file(uploadFile.getName())
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

    @Override
    public List<TreatmentDTO> getAllServiceTreatment(LocalDate startDate, LocalDate endDate) {
        Specification<ServiceTreatmentSlip> specification = SpecificationSearch.findAllServicetreatmentByClientAuthentication(startDate, endDate);
        List<ServiceTreatmentSlip> serviceTreatmentSlipList = serviceTreatmenSlipRepository.findAll(specification);
        return serviceTreatmentSlipList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReqResChangeStatusTreatment changeStatusTreatment(ReqResChangeStatusTreatment reqResChangeStatusTreatment) {
        validationService.validate(reqResChangeStatusTreatment);

        ReqResChangeStatusTreatment response = new ReqResChangeStatusTreatment();

        ServiceTreatmentSlip serviceTreatmentSlip = serviceTreatmenSlipRepository.findById(reqResChangeStatusTreatment.getIdTreatment())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Treatment not found"));

        if (serviceTreatmentSlip.isStatus()){
            serviceTreatmentSlip.setStatus(false);
            response.setMessage("Completed");
        }else {
            serviceTreatmentSlip.setStatus(true);
            response.setMessage("Uncompleted");
        }

        serviceTreatmenSlipRepository.save(serviceTreatmentSlip);

        return response;
    }

    @Override
    public ReqResSuggestionTreatment createSuggestion(ReqResSuggestionTreatment reqResSuggestionTreatment) {
        validationService.validate(reqResSuggestionTreatment);

        ServiceTreatmentSlip serviceTreatmentSlip = serviceTreatmenSlipRepository.findById(reqResSuggestionTreatment.getIdTreatment())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Treatment not found"));

        serviceTreatmentSlip.setSaranClient(reqResSuggestionTreatment.getSuggestion());

        serviceTreatmenSlipRepository.save(serviceTreatmentSlip);
        return ReqResSuggestionTreatment.builder()
                .message("Success create suggestion")
                .suggestion(serviceTreatmentSlip.getSaranClient())
                .build();
    }

    @Override
    public byte[] downloadFileClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        Client client = (Client) clientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Client Not Found "));

        Optional<UploadFile> uploadFile = Optional.ofNullable(uploadFileRepository.findByClient(client)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found")));

        byte[] fileInBytes = FileUtils.decompressFile(uploadFile.get().getFileData());
        return fileInBytes;
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

