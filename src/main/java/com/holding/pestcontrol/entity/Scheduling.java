package com.holding.pestcontrol.entity;

import com.holding.pestcontrol.enumm.FreqType;
import com.holding.pestcontrol.enumm.WorkingType;
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
@Table(name = "scheduling")
public class Scheduling {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "nama_perusahaan")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker", referencedColumnName = "nama_lengkap")
    private Worker worker;

    private LocalDate startContractPeriod;

    private LocalDate endContractPeriod;

    @Enumerated(EnumType.STRING)
    private WorkingType workingType;

    private int freq;

    @Enumerated(EnumType.STRING)
    private FreqType freqType;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String pic;

    private String noTelpPic;

    private LocalDate dateWorking;

}
