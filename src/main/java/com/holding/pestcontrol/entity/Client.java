package com.holding.pestcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "client_detail",
        indexes = @Index(name = "fk_nama_perusahaan", columnList = "nama_perusahaan")
)
public class Client {
    @Id
    private String id;

    @Column(name = "nama_perusahaan")
    private String namaPerusahaan;

    private String alamat;

    @Column(name = "no_telp")
    private String noTelp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "email")
    private User user;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
