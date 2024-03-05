package com.holding.pestcontrol.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResUpdateScheduling {

    private int statusCode;

    private String message;

    @NotBlank
    private String id;

    @NotBlank
    private String companyName;

    @NotBlank
    private String workerName;

    private ReqResSchedulingData data;
}
