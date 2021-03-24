package com.emailplatform.userservice.api;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailplatform.userservice.DataContext;
import com.emailplatform.userservice.domain.entity.QuotaType;
import com.emailplatform.userservice.service.UserAccountCommandService;
import com.emailplatform.userservice.service.UserAccountMapper;
import com.emailplatform.userservice.service.UserAccountQueryService;
import com.emailplatform.userservice.service.command.UpdateAccountQuotaCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class UserServiceRestController {

    private final UserAccountQueryService userAccountQueryService;
    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountApiMapper mapper;
    private final DataContext dataContext;

    @PostMapping("/upgrade")
    public ResponseEntity<Void> upgradeUserQuota(@RequestBody @Valid UpdateAccountQuotaRequest request) {
        val command = UpdateAccountQuotaCommand.builder()
                .userReference(dataContext.getCurrentUser())
                .subscriptionType(QuotaType.valueOf(request.getQuota()))
                .email(request.getEmail())
                .build();
        userAccountCommandService.plusQuota(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserSummaryResponse> getUserSummary() {
        val result = userAccountQueryService.getUserSummary(dataContext.getCurrentUser());
        val response = mapper.map(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/account")
    public ResponseEntity<UserAccountSummaryResponse> getAccountSummary(@RequestBody @Valid AccountSummaryRequest request) {
        val result = userAccountQueryService.getUserAccountSummary(dataContext.getCurrentUser(), request.getEmail());
        val response = mapper.map(result);
        return ResponseEntity.ok(response);
    }
}
