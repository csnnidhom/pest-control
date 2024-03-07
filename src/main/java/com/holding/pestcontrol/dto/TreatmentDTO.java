package com.holding.pestcontrol.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreatmentDTO {

    private String message;

    private String idTreatment;

    private String idSchedule;

    private List<SchedulingDTO> schedulingDTO;

    private String area;

    private List<TreatmentTypeDTO> treatmentTypeDTO;

    private String ai;

    private String rekarks;

    private List<ChemicalDTO> chemicalDTO;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String rekomendasiWorker;

    private String saranClient;

}
