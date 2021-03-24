package com.emailplatform.userservice.service.command;

import com.emailplatform.userservice.domain.entity.QuotaType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAccountQuotaCommand {

    private String userReference;
    private String email;
    private QuotaType subscriptionType;
}
