package com.emailplatform.userservice.auto;

import static org.assertj.core.api.Assertions.assertThat;

import static com.emailplatform.userservice.utils.FormattingUtils.formatQuota;
import static com.emailplatform.userservice.utils.FormattingUtils.getFormattedDate;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.service.UserAccountQueryService;
import com.emailplatform.userservice.service.vo.Quota;
import com.emailplatform.userservice.service.vo.UserAccountSummary;
import com.emailplatform.userservice.service.vo.UserSummary;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

public class AssertionsStepDefs {

    @Autowired
    private UserAccountQueryService userAccountQueryService;

    @Autowired
    private TestDataContext testUserInformation;

    @Then("User has {string} active quota for account {string}")
    public void checkActiveQuota(String quotaType, String email) {
        Quota quota = userAccountQueryService.getQuotaDetails(QuotaType.valueOf(quotaType));
        String userReference = testUserInformation.getCurrentUser();
        UserAccountSummary result = userAccountQueryService.getUserAccountSummary(userReference, email);
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getTotalSpace()).isEqualTo(formatQuota(quota.getSpaceGB()));
        assertThat(result.getLatestSubscription()).isEqualTo(quota.getType().name());
        assertThat(result.getSubscriptionStartDate()).isEqualTo(getFormattedDate(LocalDateTime.now()));

        if (quota.getPeriodDays() > 0) {
            assertThat(result.getSubscriptionEndDate()).isEqualTo(getFormattedDate(LocalDateTime.now().plusDays(quota.getPeriodDays())));
        } else {
            assertThat(result.getSubscriptionEndDate()).isEqualTo("UNLIMITED");
        }
    }

    @Then("Quota subscription for {string} is active from today")
    public void checkQuotaActiveFromToday(String email) {
        UserAccountSummary summary = userAccountQueryService.getUserAccountSummary(testUserInformation.getCurrentUser(), email);
        assertThat(summary.getSubscriptionStartDate()).isEqualTo(getFormattedDate(LocalDateTime.now()));
    }

    @Then("Quota subscription for {string} is unlimited")
    public void checkQuotaUnlimited(String email) {
        UserAccountSummary summary = userAccountQueryService.getUserAccountSummary(testUserInformation.getCurrentUser(), email);
        assertThat(summary.getSubscriptionEndDate()).isEqualTo("UNLIMITED");
    }

    @Then("Quota subscription for {string} expires after {int} days")
    public void checkQuotaExpirationAfterDays(String email, int days) {
        UserAccountSummary summary = userAccountQueryService.getUserAccountSummary(testUserInformation.getCurrentUser(), email);
        assertThat(summary.getSubscriptionEndDate()).isEqualTo(getFormattedDate(LocalDateTime.now().plusDays(days)));
    }

    @Then("Quota subscription for {string} is expired {int} days ago")
    public void checkQuotaExpiredBeforeDays(String email, int days) {
        UserAccountSummary summary = userAccountQueryService.getUserAccountSummary(testUserInformation.getCurrentUser(), email);
        assertThat(summary.getSubscriptionEndDate()).isEqualTo(getFormattedDate(LocalDateTime.now().minusDays(days)));
    }

    @Then("Summary data for account {string} is")
    public void checkAccountSummary(String email, DataTable dataTable) {
        final Map<String, String> testData = dataTable.asMap(String.class, String.class);
        UserAccountSummary summary = userAccountQueryService.getUserAccountSummary(testUserInformation.getCurrentUser(), email);

        assertThat(summary.getEmail()).isEqualTo(testData.get("email"));
        assertThat(summary.getOwner()).isEqualTo(testData.get("owner"));
        assertThat(summary.getUsedSpace()).isEqualTo(testData.get("usedGB"));
        assertThat(summary.getTotalSpace()).isEqualTo(testData.get("totalGB"));
        assertThat(summary.getLatestSubscription()).isEqualTo(testData.get("latestSubscription"));
        assertThat(summary.getPrice()).isEqualTo(testData.get("price"));

        assertThat(summary.getFreeSpace()).isEqualTo(testData.get("freeSpace"));
    }

    @Then("Summary data for current user is")
    public void checkUserSummary(DataTable dataTable) {
        final Map<String, String> testData = dataTable.asMap(String.class, String.class);
        UserSummary summary = userAccountQueryService.getUserSummary(testUserInformation.getCurrentUser());

        assertThat(summary.getName()).isEqualTo(testData.get("owner"));
        assertThat(summary.getCurrentCosts()).isEqualTo(testData.get("currentCosts"));
        assertThat(summary.getTotalQuotaSpace()).isEqualTo(testData.get("totalQuotaGB"));
        assertThat(summary.getTotalUsedSpace()).isEqualTo(testData.get("totalUsedGB"));
        assertThat(summary.getEmails()).isEqualTo(testData.get("emails"));
    }
}