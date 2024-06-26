package com.holding.pestcontrol.dto.profileUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.UploadFile;
import com.holding.pestcontrol.entity.User;
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
public class ReqResClient {

    private String message;

    private String id;

    @NotBlank
    private String namaPerusahaan;

    private String alamat;

    private String noTelp;

    private Client detailClient;

    private String file;

}
