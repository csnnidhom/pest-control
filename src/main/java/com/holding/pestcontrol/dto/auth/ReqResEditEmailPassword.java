package com.holding.pestcontrol.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ReqResEditEmailPassword {

    private String message;

    @NotBlank
    @Email
    private String oldEmail;

    @NotBlank
    @Email
    private String newEmail;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
