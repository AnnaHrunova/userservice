package com.emailplatform.userservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emailplatform.userservice.domain.entity.AccountEntity;
import com.emailplatform.userservice.domain.entity.QuotaEntity;
import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.domain.entity.UserEntity;
import com.emailplatform.userservice.domain.repository.AccountRepository;
import com.emailplatform.userservice.domain.repository.QuotaRepository;
import com.emailplatform.userservice.domain.repository.UserRepository;
import com.emailplatform.userservice.service.command.CreateNewUserCommand;
import com.emailplatform.userservice.service.command.UpdateAccountQuotaCommand;
import com.emailplatform.userservice.service.vo.secret.AccountData;
import com.emailplatform.userservice.service.vo.secret.UserData;
import com.emailplatform.userservice.utils.DataReferenceGenerator;
import com.emailplatform.userservice.utils.DateTimeProvider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;

@Service
@AllArgsConstructor
@Transactional
public class UserAccountCommandService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final QuotaRepository quotaRepository;
    private final DateTimeProvider dateTimeProvider;
    private final GdprDataService gdprDataService;
    private final DataReferenceGenerator dataReferenceGenerator;

    public String createNewUserWithEmailAccounts(CreateNewUserCommand command) {
        val user = userRepository.save(new UserEntity());
        val userReference = user.getUserReference();
        val emails = command.getEmails();
        val userData = UserData.builder()
                .firstName(command.getFirstName())
                .middleName(command.getMiddleName())
                .lastName(command.getLastName())
                .build();
        gdprDataService.saveUserData(userReference, userData);
        IntStream.range(0, emails.size())
                .forEach(i -> addNewAccount(userReference, emails.get(i)));
        return userReference;
    }

    public void plusQuota(UpdateAccountQuotaCommand command) {
        val subscription = quotaRepository.findByType(command.getSubscriptionType()).orElseThrow();
        val accountReference = dataReferenceGenerator.getAccountReference(command.getEmail());
        val account = accountRepository.findByAccountReferenceAndUserUserReference(accountReference, command.getUserReference()).orElseThrow();
        updateSubscriptionData(account, subscription);
        accountRepository.save(account);
    }

    public void minusQuota(String email, String userReference, BigDecimal usedQuota) {
        val accountReference = dataReferenceGenerator.getAccountReference(email);
        val account = accountRepository.findByAccountReferenceAndUserUserReference(accountReference, userReference).orElseThrow();
        account.setUsedQuotaGB(usedQuota);
        accountRepository.save(account);
    }

    private void updateSubscriptionData(AccountEntity account, QuotaEntity subscription) {
        account.setLatestSubscription(subscription);
        account.setSubscriptionStartDate(dateTimeProvider.getNowLocal());

        LocalDateTime subscriptionEndDate = null;
        if (subscription.getPeriodDays() > 0) {
            subscriptionEndDate = dateTimeProvider.getNowLocalPlusDays(subscription.getPeriodDays());
        }
        account.setSubscriptionEndDate(subscriptionEndDate);
    }

    private void addNewAccount(String userReference, String email) {
        val accountReference = dataReferenceGenerator.getAccountReference(email);
        if (accountRepository.findByAccountReference(accountReference).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        if (email.contains("expired")) {
            addAccountWithExpiredSubscription(userReference, email);
        } else {
            addDefaultAccount(userReference, email, accountReference);
        }
    }

    private void addDefaultAccount(String userReference, String email, String accountReference) {
        val subscription = quotaRepository.findByType(QuotaType.FREE_TEAR).orElseThrow();
        val user = userRepository.findByUserReference(userReference).orElseThrow();
        val account = new AccountEntity(user, accountReference);
        val accountData = AccountData.builder()
                .email(email)
                .build();
        updateSubscriptionData(account, subscription);
        gdprDataService.saveAccountData(account.getAccountReference(), accountData);
        accountRepository.save(account);
    }

    private void addAccountWithExpiredSubscription(String userReference, String email) {
        val subscription = quotaRepository.findByType(QuotaType.BASIC).orElseThrow();
        val user = userRepository.findByUserReference(userReference).orElseThrow();
        val accountReference = dataReferenceGenerator.getAccountReference(email);
        if (accountRepository.findByAccountReference(accountReference).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        val account = new AccountEntity(user, accountReference);
        val accountData = AccountData.builder()
                .email(email)
                .build();
        account.setSubscriptionStartDate(dateTimeProvider.getNowLocal().minusDays(40));
        account.setSubscriptionEndDate(dateTimeProvider.getNowLocal().minusDays(10));
        account.setLatestSubscription(subscription);
        account.setUsedQuotaGB(new BigDecimal("0.5"));
        gdprDataService.saveAccountData(account.getAccountReference(), accountData);
        accountRepository.save(account);
    }
}
