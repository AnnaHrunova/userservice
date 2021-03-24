package com.emailplatform.userservice.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;

@Component
@RequiredArgsConstructor
public class DataReferenceGenerator {


    @Setter
    @Value("${email.salt:N/A}")
    private String emailSalt;


    @Setter
    @Value("${user.salt:N/A}")
    private String userSalt;


    public String getAccountReference(String email) {
        val data = String.format("%s:%s", email, emailSalt);
        return DigestUtils.sha256Hex(data);
    }
}
