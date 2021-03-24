package com.emailplatform.userservice;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Profile("local")
@Data
public class DataContext {

    private String currentUser;

}
