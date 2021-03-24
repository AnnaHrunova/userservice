package com.emailplatform.userservice.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountSummaryResponse {
    private String owner;
    private String email;

    private String totalSpace;
    private String usedSpace;
    private String freeSpace;
    private String price;

    private String latestSubscription;
    private String subscriptionStartDate;
    private String subscriptionEndDate;
}
