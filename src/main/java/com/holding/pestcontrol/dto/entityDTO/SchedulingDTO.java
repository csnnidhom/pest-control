package com.holding.pestcontrol.dto.entityDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.WorkingType;
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
public class SchedulingDTO {

    private String idSchedule;

    private List<ClientDTO> client;

    private List<WorkerDTO> worker;

    private LocalDate startContractPeriod;

    private LocalDate endContractPeriod;

    private WorkingType workingType;

    private Integer freq;

    private FreqType freqType;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String pic;

    private String noTelpPic;

    private LocalDate dateWorking;
}
