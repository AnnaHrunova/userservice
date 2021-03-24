package com.emailplatform.userservice.api;

import org.springframework.stereotype.Component;

import com.emailplatform.userservice.service.vo.UserAccountSummary;
import com.emailplatform.userservice.service.vo.UserSummary;

@Component
public class UserAccountApiMapper {

    UserAccountSummaryResponse map(UserAccountSummary from) {
        return UserAccountSummaryResponse.builder()
                .owner(from.getOwner())
                .email(from.getEmail())
                .latestSubscription(from.getLatestSubscription())
                .subscriptionStartDate(from.getSubscriptionStartDate())
                .subscriptionEndDate(from.getSubscriptionEndDate())
                .totalSpace(from.getTotalSpace())
                .usedSpace(from.getUsedSpace())
                .freeSpace(from.getFreeSpace())
                .price(from.getPrice())
                .build();

    }

    UserSummaryResponse map(UserSummary userSummary) {
        return UserSummaryResponse.builder()
                .name(userSummary.getName())
                .currentCosts(userSummary.getCurrentCosts())
                .totalQuotaSpace(userSummary.getTotalQuotaSpace())
                .totalUsedSpace(userSummary.getTotalUsedSpace())
                .emails(userSummary.getEmails())
                .build();
    }

    private UserAccountResponse mapAccount(UserAccountSummary account) {
        return UserAccountResponse.builder()
                .email(account.getEmail())
                .latestSubscription(account.getLatestSubscription())
                .subscriptionStartDate(account.getSubscriptionStartDate())
                .subscriptionEndDate(account.getSubscriptionEndDate())
                .totalSpace(account.getTotalSpace())
                .usedSpace(account.getUsedSpace())
                .freeSpace(account.getFreeSpace())
                .build();
    }
}
