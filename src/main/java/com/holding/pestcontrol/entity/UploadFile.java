package com.holding.pestcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "uploadFile")
public class UploadFile {

    @Id
    private String id;

    private String name;

    @JsonIgnore
    private String type;

    @JsonIgnore
    @Lob()
    @Column(name = "data", length = 2048000)
    private byte[] fileData;

    @OneToOne
    @JoinColumn(name = "client", referencedColumnName = "nama_perusahaan")
    private Client client;

}
