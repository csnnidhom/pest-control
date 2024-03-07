package com.holding.pestcontrol.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreatmentTypeDTO {

    private String idTreatmentType;

    private boolean cf;

    private boolean hf;

    private boolean s;

    private boolean b;

    private boolean lv;

    private boolean t;

    private boolean ot;

}
