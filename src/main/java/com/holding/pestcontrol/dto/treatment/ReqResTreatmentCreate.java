package com.holding.pestcontrol.dto.treatment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.holding.pestcontrol.dto.entityDTO.ChemicalDTO;
import com.holding.pestcontrol.dto.entityDTO.TreatmentTypeDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqResTreatmentCreate {

    private String message;

    private String idTreatment;

    @NotBlank
    private String idSchedule;

    private String area;

    private TreatmentTypeDTO treatmentTypeDTO;

    private String ai;

    private String rekarks;

    private ChemicalDTO chemicalDTO;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String rekomendasiWorker;

    private String saranClient;

}
