package com.holding.pestcontrol.dto.treatment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.holding.pestcontrol.dto.entityDTO.ChemicalDTO;
import com.holding.pestcontrol.dto.entityDTO.TreatmentTypeDTO;
import jakarta.validation.Valid;
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
public class ReqResTreatmentUpdate {

    private String message;

    @NotBlank
    private String idTreatment;

    private String area;

    @Valid
    private TreatmentTypeDTO treatmentTypeDTO;

    private String ai;

    private String rekarks;

    @Valid
    private ChemicalDTO chemicalDTO;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String rekomendasiWorker;

    private String saranClient;

}
