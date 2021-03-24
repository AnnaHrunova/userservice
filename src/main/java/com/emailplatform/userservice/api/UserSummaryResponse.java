package com.emailplatform.userservice.api;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummaryResponse {

    private String name;
    private String totalQuotaSpace;
    private String totalUsedSpace;
    private String currentCosts;
    private String emails;
}
