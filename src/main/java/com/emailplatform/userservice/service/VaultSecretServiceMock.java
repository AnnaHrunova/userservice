package com.emailplatform.userservice.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VaultSecretServiceMock {

    private final HashMap<String, String> secretData;

    public void storeSecret(String path, String data) {
        secretData.put(path, data);
    }

    public String getSecret(String path) {
        return secretData.get(path);
    }

}
