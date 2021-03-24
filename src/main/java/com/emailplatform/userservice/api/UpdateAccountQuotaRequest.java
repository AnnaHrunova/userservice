package com.emailplatform.userservice.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountQuotaRequest {

    @NotBlank
    @Length(max = 100)
    @Email
    private String email;

    @NotBlank
    @Length(max = 20)
    private String quota;
}
