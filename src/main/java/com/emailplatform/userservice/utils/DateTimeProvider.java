package com.emailplatform.userservice.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class DateTimeProvider {

    public LocalDateTime getNowLocal() {
        return LocalDateTime.now();
    }

    public boolean isAfterToday(LocalDateTime date) {
        return date.compareTo(getNowLocal()) > 0;
    }

    public boolean isBeforeToday(LocalDateTime date) {
        return date.compareTo(getNowLocal()) < 0;
    }

    public LocalDateTime getNowLocalPlusDays(int days) {
        return getNowLocal().plusDays(days);
    }

}
