package com.holding.pestcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "worker_detail",
        indexes = @Index(name = "fk_nama_lengkap", columnList = "nama_lengkap")
)
public class Worker {

    @Id
    private String id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "no_telp")
    private String noTelp;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
