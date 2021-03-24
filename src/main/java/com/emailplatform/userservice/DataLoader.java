package com.emailplatform.userservice;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.emailplatform.userservice.utils.InitDataFixture;

import lombok.AllArgsConstructor;
import lombok.val;

@Component
@AllArgsConstructor
@Profile("local")
public class DataLoader implements ApplicationRunner {

    private final InitDataFixture initDataFixture;
    private final DataContext dataContext;

    @Override
    public void run(ApplicationArguments args) {
        val currentUser = initDataFixture.initUserDemoData(Arrays.asList("email1@email.com",
                "email2@email.com", "expired_subscription@email.com"));
        dataContext.setCurrentUser(currentUser);
    }
}
