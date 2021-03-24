package com.emailplatform.userservice.service;

import static java.util.stream.Collectors.toList;

import static com.emailplatform.userservice.utils.FormattingUtils.formatPrice;
import static com.emailplatform.userservice.utils.FormattingUtils.formatQuota;
import static com.emailplatform.userservice.utils.FormattingUtils.formatUserName;
import static com.emailplatform.userservice.utils.FormattingUtils.getDecimalFormat;
import static com.emailplatform.userservice.utils.FormattingUtils.getFormattedDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.emailplatform.userservice.domain.entity.AccountEntity;
import com.emailplatform.userservice.domain.entity.QuotaEntity;
import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.domain.entity.UserEntity;
import com.emailplatform.userservice.service.vo.Quota;
import com.emailplatform.userservice.service.vo.UserAccountSummary;
import com.emailplatform.userservice.service.vo.UserSummary;
import com.emailplatform.userservice.utils.DateTimeProvider;
import com.emailplatform.userservice.utils.FormattingUtils;

import lombok.AllArgsConstructor;
import lombok.val;

@Component
@AllArgsConstructor
public class UserAccountMapper {

    private final GdprDataService gdprDataService;
    private final DateTimeProvider dateTimeProvider;

    Quota map(QuotaEntity from) {
        return Quota.builder()
                .spaceGB(from.getQuotaGB())
                .type(from.getType())
                .periodDays(from.getPeriodDays())
                .build();
    }

    UserAccountSummary map(UserEntity user, AccountEntity account, BigDecimal defaultQuota) {
        val userSensitiveData = gdprDataService.getUserData(user.getUserReference());
        val accountSensitiveData = gdprDataService.getAccountData(account.getAccountReference());
        val accountQuota = account.getLatestSubscription();
        String freeSpace = calculateFreeSpace(account.getUsedQuotaGB(), accountQuota.getQuotaGB());
        if (accountQuota.getType() != QuotaType.FREE_TEAR && dateTimeProvider.isBeforeToday(account.getSubscriptionEndDate())) {
            freeSpace = calculateFreeSpace(account.getUsedQuotaGB(), defaultQuota);
        }
        return UserAccountSummary.builder()
                .owner(formatUserName(userSensitiveData))
                .email(accountSensitiveData.getEmail())
                .latestSubscription(account.getLatestSubscription().getType().name())
                .subscriptionStartDate(getFormattedDate(account.getSubscriptionStartDate()))
                .subscriptionEndDate(getFormattedDate(account.getSubscriptionEndDate()))
                .totalSpace(formatQuota(accountQuota.getQuotaGB()))
                .usedSpace(formatQuota(account.getUsedQuotaGB()))
                .freeSpace(freeSpace)
                .price(formatPrice(accountQuota.getPriceUSD(), accountQuota.getPeriodDays()))
                .build();
    }

    UserSummary map(UserEntity user, List<AccountEntity> accounts) {
        val userSensitiveData = gdprDataService.getUserData(user.getUserReference());
        val emails = accounts
                .stream().map(account -> gdprDataService.getAccountData(account.getAccountReference()).getEmail())
                .collect(toList());
        return UserSummary.builder()
                .emails(FormattingUtils.formatList(emails))
                .name(formatUserName(userSensitiveData))
                .build();
    }

    private String calculateFreeSpace(BigDecimal usedGB, BigDecimal quotaGB) {
        BigDecimal freeSpaceRate = BigDecimal.ZERO;
        if (usedGB.compareTo(BigDecimal.ZERO) == 0) {
            freeSpaceRate = BigDecimal.ONE;
        } else if (usedGB.compareTo(quotaGB) < 0) {
            freeSpaceRate = BigDecimal.ONE
                    .subtract(usedGB.divide(quotaGB, RoundingMode.HALF_UP));
        }
        val formattedFreeSpace = getDecimalFormat().format(freeSpaceRate.multiply(new BigDecimal(100)));
        return formattedFreeSpace + " %";
    }
}
