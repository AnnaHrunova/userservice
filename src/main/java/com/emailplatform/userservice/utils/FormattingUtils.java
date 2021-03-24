package com.emailplatform.userservice.utils;

import static java.util.Optional.ofNullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

import com.emailplatform.userservice.service.vo.secret.UserData;

import lombok.val;

public class FormattingUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static DecimalFormat getDecimalFormat() {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        formatSymbols.setGroupingSeparator(' ');
        return new DecimalFormat("#,##0.00", formatSymbols);
    }


    public static String formatQuota(BigDecimal number) {
        val formattedNumber =  number != null ? getDecimalFormat().format(number) : "0,00";
        return formattedNumber + " GB";
    }

    public static String formatPrice(BigDecimal number, int days) {
        val formattedNumber =  number != null && number.compareTo(BigDecimal.ZERO) > 0 ? getDecimalFormat().format(number) : "FREE";
        return formattedNumber.equals("FREE") ? formattedNumber : new StringJoiner(" ").add(formattedNumber)
                .add("USD")
                .add("per")
                .add(String.valueOf(days))
                .add("days")
                .toString();
    }

    public static String formatPrice(BigDecimal number) {
        val formattedNumber =  number != null && number.compareTo(BigDecimal.ZERO) > 0 ? getDecimalFormat().format(number) : "0,00";
        return formattedNumber + " USD";
    }

    public static String getFormattedDate(LocalDateTime from) {
        return ofNullable(from).map(FORMATTER::format)
                .orElse("UNLIMITED");
    }

    public static String formatUserName(UserData from) {
        StringJoiner result = new StringJoiner(" ");
        result.add(from.getFirstName());
        ofNullable(from.getMiddleName())
                .ifPresent(result::add);
        result.add(from.getLastName());
        return result.toString();
    }

    public static String formatList(List<String> from) {
        return StringUtils.join(from, ", ");
    }
}
