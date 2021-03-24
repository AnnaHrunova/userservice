package com.emailplatform.userservice.auto;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.emailplatform.userservice")
@EnableJpaRepositories(basePackages = {
        "com.emailplatform.userservice.*",
}, bootstrapMode = BootstrapMode.LAZY)
@EntityScan("com.emailplatform.userservice.*")
@EnableAsync
public class IntegrationApplication {

}
