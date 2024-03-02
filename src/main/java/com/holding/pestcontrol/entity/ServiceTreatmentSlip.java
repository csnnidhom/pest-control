package com.holding.pestcontrol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_treatment_slip")
public class ServiceTreatmentSlip {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "custumer", referencedColumnName = "nama_perusahaan")
    private Client client;

    private String area;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "treatment_type", referencedColumnName = "id")
    private TreatmentType treatmentType;

    private boolean ai;

    private String rekarks;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chemical", referencedColumnName = "id")
    private Chemical chemical;

    private LocalDate date;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private String rekomendasiWorker;

    private String ttdWorker;

    private String saranClient;

    private String ttdClient;

}
