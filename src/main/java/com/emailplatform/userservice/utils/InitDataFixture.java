package com.emailplatform.userservice.utils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.emailplatform.userservice.domain.entity.QuotaEntity;
import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.domain.repository.QuotaRepository;
import com.emailplatform.userservice.domain.repository.UserRepository;
import com.emailplatform.userservice.service.UserAccountCommandService;
import com.emailplatform.userservice.service.command.CreateNewUserCommand;

import lombok.AllArgsConstructor;
import lombok.val;

@Component
@AllArgsConstructor
public class InitDataFixture {

    private final UserAccountCommandService userAccountCommandService;
    private final QuotaRepository quotaRepository;

    public String initUserDemoData(List<String> emails) {
        initQuotaData();
        return createUserAccount(emails);
    }

    private String createUserAccount(List<String> emails) {
        val command = CreateNewUserCommand.builder()
                .emails(emails)
                .firstName("Firstname")
                .lastName("Lastname")
                .build();
        return userAccountCommandService.createNewUserWithEmailAccounts(command);
    }

    private void initQuotaData() {
        QuotaEntity freeTearQuota = new QuotaEntity();
        freeTearQuota.setType(QuotaType.FREE_TEAR);
        freeTearQuota.setQuotaGB(BigDecimal.ONE);
        freeTearQuota.setPeriodDays(0);
        freeTearQuota.setPriceUSD(BigDecimal.ZERO);
        quotaRepository.save(freeTearQuota);

        QuotaEntity basicSubscription = new QuotaEntity();
        basicSubscription.setType(QuotaType.BASIC);
        basicSubscription.setQuotaGB(new BigDecimal(10));
        basicSubscription.setPeriodDays(30);
        basicSubscription.setPriceUSD(BigDecimal.ONE);
        quotaRepository.save(basicSubscription);
    }
}
