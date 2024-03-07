package com.holding.pestcontrol.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treatment_type")
public class TreatmentType {

    @Id
    private String id;

    private boolean cf;

    private boolean hf;

    private boolean s;

    private boolean b;

    private boolean lv;

    private boolean t;

    private boolean ot;

}
