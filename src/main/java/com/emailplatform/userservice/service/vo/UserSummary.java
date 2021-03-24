package com.emailplatform.userservice.service.vo;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {

    private String name;
    private String totalQuotaSpace;
    private String totalUsedSpace;
    private String currentCosts;
    private String emails;
}
