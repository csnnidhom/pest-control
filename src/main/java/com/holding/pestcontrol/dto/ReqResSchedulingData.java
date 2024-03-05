package com.holding.pestcontrol.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResSchedulingData {

    private String id;

    private String companyName;

    private String workerName;

    private LocalDate startContractPeriod;

    private LocalDate endContractPeriod;

    private String workingType;

    private int freq;

    private String freqType;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String pic;

    private String noTelpPic;

    private LocalDate dateWorking;
}
