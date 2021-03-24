package com.emailplatform.userservice.service;

import org.springframework.stereotype.Service;

import com.emailplatform.userservice.service.vo.secret.AccountData;
import com.emailplatform.userservice.service.vo.secret.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Service
@Slf4j
@AllArgsConstructor
public class GdprDataService {

    private final VaultSecretServiceMock vaultSecretServiceMock;
    private final ObjectMapper objectMapper;

    AccountData getAccountData(String accountReference) {
        val data = vaultSecretServiceMock.getSecret(accountReference);
        try {
            return objectMapper.readValue(data, AccountData.class);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error while getting data from Vault, path={}", accountReference, e);
            return null;
        }
    }

    void saveAccountData(String accountReference, AccountData accountData) {
        try {
            val data = objectMapper.writeValueAsString(accountData);
            vaultSecretServiceMock.storeSecret(accountReference, data);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error while saving data to Vault, path={}", accountReference, e);
        }
    }

    UserData getUserData(String userReference) {
        val data = vaultSecretServiceMock.getSecret(userReference);
        try {
            return objectMapper.readValue(data, UserData.class);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error while getting data from Vault, path={}", userReference, e);
            return null;
        }
    }

    void saveUserData(String userReference, UserData userData) {
        try {
            val data = objectMapper.writeValueAsString(userData);
            vaultSecretServiceMock.storeSecret(userReference, data);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error while saving data to Vault, path={}", userReference, e);
        }
    }
}
