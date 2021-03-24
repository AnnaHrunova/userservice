package com.emailplatform.userservice.auto;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(
        classes = IntegrationApplication.class,
        properties = "classpath:application-test.yml",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext
public class CucumberSpringConfiguration {

}