package com.holding.pestcontrol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.holding.pestcontrol.entity.Scheduling;
import com.holding.pestcontrol.entity.TreatmentType;
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
public class ReqResTreatment {

    private String message;

    private String idTreatment;

    private String idSchedule;

    private Scheduling scheduling;

    private String area;

    private TreatmentType requestTreatmentType;

    private String ai;

    private String rekarks;

    private RequestChemical requestChemicalchemical;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String rekomendasiWorker;

    private String saranClient;

}
