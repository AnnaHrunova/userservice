package com.emailplatform.userservice.auto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.cucumber.spring.ScenarioScope;

@Component
@ScenarioScope
public class TestDataContext {

    private String currentUser;

    public void setCurrentUser(String userReference) {
        this.currentUser = userReference;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
