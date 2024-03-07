package com.holding.pestcontrol.service.worker.treatment;

import com.holding.pestcontrol.dto.ClientDTO;
import com.holding.pestcontrol.dto.ReqResTreatment;
import com.holding.pestcontrol.dto.SchedulingDTO;
import com.holding.pestcontrol.entity.*;
import com.holding.pestcontrol.repository.*;
import com.holding.pestcontrol.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentImpl implements Treatment{

    private final ValidationService validationService;
    private final ServiceTreatmenSlipRepository serviceTreatmenSlipRepository;
    private final SchedulingRepository schedulingRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;
    private final ChemicalRepository chemicalRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;

    @Override
    public ReqResTreatment createTreatment(ReqResTreatment reqResTreatment) {
        validationService.validate(reqResTreatment);

        ReqResTreatment response = new ReqResTreatment();

        Scheduling scheduling = schedulingRepository.findById(reqResTreatment.getIdSchedule())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id schedule not found"));

        try{
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
            serviceTreatmentSlip.setDate(reqResTreatment.getDate());
            serviceTreatmentSlip.setTimeIn(reqResTreatment.getTimeIn());
            serviceTreatmentSlip.setTimeOut(reqResTreatment.getTimeOut());
            serviceTreatmentSlip.setRekomendasiWorker(reqResTreatment.getRekomendasiWorker());
            serviceTreatmentSlip.setSaranClient(reqResTreatment.getSaranClient());
            serviceTreatmenSlipRepository.save(serviceTreatmentSlip);

            response.setMessage("Success add treatment");
            response.setIdTreatment(serviceTreatmentSlip.getId());
        }catch (Exception e){
            response.setMessage("Failed add treatment -> " + e.getMessage());
        }

        return response;
    }
}
