package com.holding.pestcontrol.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifikasiEmail {

    @NotBlank
    private  String email;

    @NotBlank
    @Size(max = 6)
    private String otp;
}
