package com.holding.pestcontrol.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startContractPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
