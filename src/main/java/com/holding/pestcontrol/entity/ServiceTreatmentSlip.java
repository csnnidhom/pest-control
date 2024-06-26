package com.holding.pestcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_scheduling", referencedColumnName = "id")
    private Scheduling scheduling;

    private String area;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_treatment_type", referencedColumnName = "id")
    private TreatmentType treatmentType;

    private boolean ai;

    private String rekarks;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_chemical", referencedColumnName = "id")
    private Chemical chemical;

    @Column(name = "date_working")
    private LocalDate dateWorking;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "rekomendasi_worker")
    private String rekomendasiWorker;

    @Column(name = "saran_client")
    private String saranClient;

    private boolean status;

}
