package com.emailplatform.userservice.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountResponse {
    private String email;

    private String totalSpace;
    private String usedSpace;
    private String freeSpace;

    private String latestSubscription;
    private String subscriptionStartDate;
    private String subscriptionEndDate;
}
