package com.emailplatform.userservice.auto;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.service.UserAccountCommandService;
import com.emailplatform.userservice.service.UserAccountQueryService;
import com.emailplatform.userservice.service.command.UpdateAccountQuotaCommand;
import com.emailplatform.userservice.service.vo.UserAccountSummary;
import com.emailplatform.userservice.service.vo.UserSummary;
import com.emailplatform.userservice.utils.InitDataFixture;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class StepDefs {

    @Autowired
    private UserAccountCommandService userAccountCommandService;

    @Autowired
    private UserAccountQueryService userAccountQueryService;

    @Autowired
    private InitDataFixture initDataFixture;

    @Autowired
    private TestDataContext testUserInformation;

    @Given("Registered user with email accounts: {string} and {string} and {string}")
    public void createUserWithAccounts(String email1, String email2, String email3) {
        List<String> emails = Arrays.asList(email1, email2, email3);
        String userReference = initDataFixture.initUserDemoData(emails);
        testUserInformation.setCurrentUser(userReference);
    }

    @When("User adds {string} quota for account {string}")
    public void userUpgradesQuota(String subscriptionType, String email) {
        QuotaType quotaType = QuotaType.valueOf(subscriptionType);
        UpdateAccountQuotaCommand command = UpdateAccountQuotaCommand.builder()
                .userReference(testUserInformation.getCurrentUser())
                .subscriptionType(quotaType)
                .email(email)
                .build();
        userAccountCommandService.plusQuota(command);
    }

    @When("User uses {int} GB in account {string}")
    public void userUsesFreeSpace(int usedGB, String email) {
        userAccountCommandService.minusQuota(email, testUserInformation.getCurrentUser(), new BigDecimal(usedGB));
    }

    @When("Support Agent gets {string} account information")
    public void getAccountSummary(String email) {
        String userReference = testUserInformation.getCurrentUser();
        UserAccountSummary result = userAccountQueryService.getUserAccountSummary(userReference, email);
        assertThat(result).isNotNull();
    }

    @When("Support Agent gets current user information")
    public void getUserSummary() {
        String userReference = testUserInformation.getCurrentUser();
        UserSummary result = userAccountQueryService.getUserSummary(userReference);
        assertThat(result).isNotNull();
    }
}