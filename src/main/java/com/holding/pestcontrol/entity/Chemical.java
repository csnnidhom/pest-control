package com.holding.pestcontrol.entity;

import jakarta.persistence.Column;
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
@Table(name = "chemical")
public class Chemical {

    @Id
    private String id;

    @Column(name = "bahan_aktif")
    private String bahanAktif;

    private String dosis;

    private String jumlah;

    private String keterangan;
}
