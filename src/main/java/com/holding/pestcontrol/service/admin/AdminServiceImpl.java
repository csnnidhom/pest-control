package com.holding.pestcontrol.service.admin;

import com.holding.pestcontrol.dto.entityDTO.*;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminCreateDetailUser;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminGetDelete;
import com.holding.pestcontrol.dto.profileUser.ReqResAdminUpdateDetailUser;
import com.holding.pestcontrol.dto.response.ResponseSearchClient;
import com.holding.pestcontrol.dto.response.ResponseSearchWorker;
import com.holding.pestcontrol.dto.schedule.ReqResCreateScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResDeleteScheduling;
import com.holding.pestcontrol.dto.schedule.ReqResSchedulingData;
import com.holding.pestcontrol.dto.schedule.ReqResUpdateScheduling;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.Role;
import com.holding.pestcontrol.enumm.WorkingType;
import com.holding.pestcontrol.repository.*;
import com.holding.pestcontrol.service.SpecificationSearch;
import com.holding.pestcontrol.service.ValidationService;
import com.holding.pestcontrol.util.FileUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{

    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final WorkerRepository workerRepository;
    private final SchedulingRepository schedulingRepository;
    private final ServiceTreatmenSlipRepository serviceTreatmenSlipRepository;
    private final UploadFileRepository uploadFileRepository;

    private static long MAX_FILE_SIZE = 2048000; //2Mb

    public boolean isValidFileSize(MultipartFile file){
        return file.getSize() <= MAX_FILE_SIZE;
    }

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
    public ReqResAdminCreateDetailUser createDetail(ReqResAdminCreateDetailUser reqResAdminCreateDetailUser) {
        validationService.validate(reqResAdminCreateDetailUser);

        ReqResAdminCreateDetailUser response = new ReqResAdminCreateDetailUser();

        User user = userRepository.findByEmail(reqResAdminCreateDetailUser.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        String role = user.getRole().name();

        if (role.equals("CLIENT")){
            if (reqResAdminCreateDetailUser.getNamaPerusahaan() == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama perusahaan is null");
            }
            Client client = new Client();
            client.setId(UUID.randomUUID().toString());
            client.setUser(user);
            client.setNamaPerusahaan(reqResAdminCreateDetailUser.getNamaPerusahaan());
            client.setAlamat(reqResAdminCreateDetailUser.getAlamat());
            client.setNoTelp(reqResAdminCreateDetailUser.getNoTelp());
            clientRepository.save(client);
            response.setMessage("Success insert detail client");

        } else if (role.equals("WORKER")) {
            if (reqResAdminCreateDetailUser.getNamaLengkap() == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama Lengkap is null");
            }
            Worker worker = new Worker();
            worker.setId(UUID.randomUUID().toString());
            worker.setUser(user);
            worker.setNamaLengkap(reqResAdminCreateDetailUser.getNamaLengkap());
            worker.setAlamat(reqResAdminCreateDetailUser.getAlamat());
            worker.setNoTelp(reqResAdminCreateDetailUser.getNoTelp());
            workerRepository.save(worker);
            response.setMessage("Success insert detail worker");
        } else {
            response.setMessage("You are ADMIN");
        }
        return response;
    }

    @Override
    public ReqResAdminUpdateDetailUser updateDetail(ReqResAdminUpdateDetailUser reqResAdminUpdateDetailUser) {
        validationService.validate(reqResAdminUpdateDetailUser);

        ReqResAdminUpdateDetailUser response = new ReqResAdminUpdateDetailUser();

        User user = userRepository.findByEmail(reqResAdminUpdateDetailUser.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));

        String role = user.getRole().name();

        if (role.equals("CLIENT")){
            Client client = (Client) clientRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

            client.setNamaPerusahaan(reqResAdminUpdateDetailUser.getNamaPerusahaan());
            client.setAlamat(reqResAdminUpdateDetailUser.getAlamat());
            client.setNoTelp(reqResAdminUpdateDetailUser.getNoTelp());
            clientRepository.save(client);
            response.setMessage("Success update detail client");

        } else if (role.equals("WORKER")) {
            Worker worker = (Worker) workerRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

            worker.setNamaLengkap(reqResAdminUpdateDetailUser.getNamaLengkap());
            worker.setNoTelp(reqResAdminUpdateDetailUser.getNoTelp());
            worker.setAlamat(reqResAdminUpdateDetailUser.getAlamat());
            workerRepository.save(worker);
            response.setMessage("Success update detail worker");
        }
        return response;
    }

    @Override
    public List<ResponseSearchClient> searchClient(String companyName, String companyAddress, String companyEmail) {
        Specification<Client> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (companyName != null && !companyName.isEmpty()){
                predicates.add(SpecificationSearch.companyNameClient(companyName).toPredicate(root,query,criteriaBuilder));
            }

            if (companyAddress != null && !companyAddress.isEmpty()){
                predicates.add(SpecificationSearch.companyAddress(companyAddress).toPredicate(root,query,criteriaBuilder));
            }

            if (companyEmail != null && !companyEmail.isEmpty()){
                predicates.add(SpecificationSearch.companyEmail(companyEmail).toPredicate(root,query,criteriaBuilder));
            }

            if (predicates.isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
//        return clientRepository.findAll(specification);

        List<Client> clients = clientRepository.findAll(specification);
        List<ResponseSearchClient> responseSearchClients = new ArrayList<>();

        for (Client client : clients){
            Optional<UploadFile> uploadFiles = uploadFileRepository.findByClient(client);
            responseSearchClients.add(new ResponseSearchClient(client, uploadFiles));
        }

        return responseSearchClients;
    }

    @Override
    public List<Worker> searchWorker(String workerFullname, String wokerAddress, String workerEmail) {
        Specification<Worker> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (workerFullname != null && !workerFullname.isEmpty()){
                predicates.add(SpecificationSearch.workerFullname(workerFullname).toPredicate(root,query,criteriaBuilder));
            }

            if (wokerAddress != null && !wokerAddress.isEmpty()){
                predicates.add(SpecificationSearch.workerAddress(wokerAddress).toPredicate(root,query,criteriaBuilder));
            }

            if (workerEmail != null && !workerEmail.isEmpty()){
                predicates.add(SpecificationSearch.workerEmail(workerEmail).toPredicate(root,query,criteriaBuilder));
            }

            if (predicates.isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));

            }
        };
        return workerRepository.findAll(specification);

    }

    @Override
    public ReqResAdminGetDelete getDetailByEmail(String email) {
        validationService.validate(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        String role = user.getRole().name();

        Client client = null;
        Worker worker = null;

        if (role.equals("CLIENT")) {
            client = (Client) clientRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail client not found"));
        }

        if (role.equals("WORKER")) {
            worker = (Worker) workerRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail worker not found"));
        }

        UploadFile uploadFile = uploadFileRepository.findByClient(client).orElse(null);

        return ReqResAdminGetDelete.builder()
                .user(user)
                .detailClient(client)
                .workerDetail(worker)
                .file(uploadFile != null ? uploadFile.getName() : null)
                .build();
    }

    @Override
    public String switchStatus(String email) {
        validationService.validate(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (!user.isStatus()){
            user.setStatus(true);
            userRepository.save(user);
            return "The account is ACTIVE";
        } else {
            user.setStatus(false);
            userRepository.save(user);
            return "The account is NOT ACTIVE";
        }

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
    public List<Scheduling> searchSchedule(String companyName, LocalDate startDate, LocalDate endDate) {
        Specification<Scheduling> specification = Specification.where(null);

        if (companyName != null && !companyName.isEmpty()){
            specification = specification.and(SpecificationSearch.companyNameSchedule(companyName));
        }

        if (startDate != null && endDate != null){
            specification = specification.and(SpecificationSearch.dateWorkingScheduling(startDate, endDate));
        }

        return schedulingRepository.findAll(specification);
    }

    @Override
    public SchedulingDTO getDataSchedulingById(String id) {
        validationService.validate(id);

        Scheduling scheduling = schedulingRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Schedul Not Found"));

        SchedulingDTO schedulingDTO = new SchedulingDTO();
        schedulingDTO.setIdSchedule(id);
        schedulingDTO.setClient(convertClientToDTO(Collections.singletonList(scheduling.getClient())));
        schedulingDTO.setWorker(convertWorkerToDTO(Collections.singletonList(scheduling.getWorker())));
        schedulingDTO.setStartContractPeriod(scheduling.getStartContractPeriod());
        schedulingDTO.setEndContractPeriod(scheduling.getEndContractPeriod());
        schedulingDTO.setWorkingType(scheduling.getWorkingType());
        schedulingDTO.setFreq(scheduling.getFreq());
        schedulingDTO.setFreqType(scheduling.getFreqType());
        schedulingDTO.setTimeStart(scheduling.getTimeStart());
        schedulingDTO.setTimeEnd(scheduling.getTimeEnd());
        schedulingDTO.setPic(scheduling.getPic());
        schedulingDTO.setNoTelpPic(scheduling.getNoTelpPic());
        schedulingDTO.setDateWorking(scheduling.getDateWorking());

        return schedulingDTO;
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

    @Override
    public List<TreatmentDTO> getAllTreatment(String companyName, LocalDate startDate, LocalDate endDate) {

        Specification<ServiceTreatmentSlip> specification = Specification.where(null);

        if (companyName != null && !companyName.isEmpty()){
            specification = specification.and(SpecificationSearch.companyNameTreatment(companyName));
        }

        if (startDate != null && endDate != null){
            specification = specification.and(SpecificationSearch.dateWorkingTreatment(startDate, endDate));
        }

        List<ServiceTreatmentSlip> serviceTreatmentSlips = serviceTreatmenSlipRepository.findAll(specification);
        return serviceTreatmentSlips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(String companyName, MultipartFile file) throws IOException {
        Client client = (Client) clientRepository.findByNamaPerusahaan(companyName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Not Found "));

        UploadFile existingFile = uploadFileRepository.findByClient(client).orElse(null);

        if (existingFile != null){
            uploadFileRepository.delete(existingFile);
        }

        if (!isValidFileSize(file)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum file size is 2 MB");
        }

        if (!file.getContentType().equals("application/pdf")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file must be in PDF format");
        }

        if (file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Select files to upload");
        }

        UploadFile newUploadFile = new UploadFile();
        newUploadFile.setId(UUID.randomUUID().toString());
        newUploadFile.setName(file.getOriginalFilename());
        newUploadFile.setType(file.getContentType());
        newUploadFile.setFileData(FileUtils.compressFile(file.getBytes()));
        newUploadFile.setClient(client);
        uploadFileRepository.save(newUploadFile);

        return "Success upload " + file.getOriginalFilename();

    }

    @Override
    public byte[] downloadFile(String fileName) {
        Optional<UploadFile> uploadFile = Optional.ofNullable(uploadFileRepository.findByName(fileName)
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
