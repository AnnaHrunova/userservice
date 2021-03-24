package com.emailplatform.userservice.service.vo;

import java.math.BigDecimal;

import com.emailplatform.userservice.domain.entity.QuotaType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Quota {

    private QuotaType type;
    private Integer periodDays;
    private BigDecimal spaceGB;
}
