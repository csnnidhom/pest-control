package com.holding.pestcontrol.dto.profileUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.Worker;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ReqResAdminCreateDetailUser {

    private String message;

    @NotBlank
    @Email
    private String email;

    private String namaPerusahaan;

    @NotBlank
    private String alamat;

    @NotBlank
    private String noTelp;

    private String namaLengkap;

    private Client client;

    private Worker worker;

}
