package com.emailplatform.userservice.service;

import static com.emailplatform.userservice.utils.FormattingUtils.*;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;

import com.emailplatform.userservice.domain.entity.AccountEntity;
import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.domain.repository.AccountRepository;
import com.emailplatform.userservice.domain.repository.QuotaRepository;
import com.emailplatform.userservice.domain.repository.UserRepository;
import com.emailplatform.userservice.service.vo.Quota;
import com.emailplatform.userservice.service.vo.UserAccountSummary;
import com.emailplatform.userservice.service.vo.UserSummary;
import com.emailplatform.userservice.utils.DataReferenceGenerator;
import com.emailplatform.userservice.utils.DateTimeProvider;
import com.emailplatform.userservice.utils.FormattingUtils;

import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
@Transactional
public class UserAccountQueryService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final QuotaRepository quotaRepository;
    private final UserAccountMapper mapper;
    private final DataReferenceGenerator dataReferenceGenerator;
    private final DateTimeProvider dateTimeProvider;

    public Quota getQuotaDetails(QuotaType type) {
        val quota = quotaRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException(String.format("Quota with type: %s not found", type.name())));
        return mapper.map(quota);
    }

    public UserAccountSummary getUserAccountSummary(String userReference, String email) {
        val user = userRepository.findByUserReference(userReference)
                .orElseThrow(() -> new RuntimeException(String.format("User with reference: %s not found", userReference)));
        val accountReference = dataReferenceGenerator.getAccountReference(email);
        val account = accountRepository.findByAccountReferenceAndUserUserReference(accountReference, userReference)
                .orElseThrow(() -> new RuntimeException(String.format("Account with reference: %s for user with reference: %s not found", accountReference, userReference)));
        val defaultQuota = getQuotaDetails(QuotaType.FREE_TEAR);
        return mapper.map(user, account, defaultQuota.getSpaceGB());
    }

    public UserSummary getUserSummary(String userReference) {
        val user = userRepository.findByUserReference(userReference)
                .orElseThrow(() -> new RuntimeException(String.format("User with reference: %s not found", userReference)));
        val accounts = accountRepository.findByUserUserReference(userReference);
        val currentTotalQuota = calculateCurrentTotalQuotaSpace(accounts);
        val totalUsedSpace = calculateTotalUsedSpace(accounts);
        val currentCosts = calculateCurrentCosts(accounts);
        val result = mapper.map(user, accounts);
        result.setCurrentCosts(currentCosts);
        result.setTotalQuotaSpace(currentTotalQuota);
        result.setTotalUsedSpace(totalUsedSpace);
        return result;
    }

    private String calculateCurrentCosts(List<AccountEntity> accounts) {
        val costs = accounts.stream()
                .filter(this::isFreeOrHasActiveSubscription)
                .map(account -> account.getLatestSubscription().getPriceUSD())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return FormattingUtils.formatPrice(costs);
    }

    private String calculateTotalUsedSpace(List<AccountEntity> accounts) {
        val usedGB = accounts.stream()
                .map(AccountEntity::getUsedQuotaGB)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return FormattingUtils.formatQuota(usedGB);
    }

    private String calculateCurrentTotalQuotaSpace(List<AccountEntity> accounts) {
        val defaultQuota = getQuotaDetails(QuotaType.FREE_TEAR);
        val totalGB = accounts.stream()
                .map(account -> isFreeOrHasActiveSubscription(account) ? account.getLatestSubscription().getQuotaGB() : defaultQuota.getSpaceGB())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return FormattingUtils.formatQuota(totalGB);
    }

    private boolean isFreeOrHasActiveSubscription(AccountEntity account) {
        return account.getSubscriptionEndDate() == null || dateTimeProvider.isAfterToday(account.getSubscriptionEndDate());
    }
}
