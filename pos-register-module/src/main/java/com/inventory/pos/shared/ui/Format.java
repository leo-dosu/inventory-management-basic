package com.inventory.pos.shared.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Format {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private Format() {
    }

    /** Naira with thousands separators and kobo: {@code ₦2,450.00}. */
    public static String money(BigDecimal value) {
        String formatted = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US))
                .format(value.abs());
        return (value.signum() < 0 ? "-" : "") + "₦" + formatted;
    }

    public static String date(LocalDate date) {
        return date == null ? "" : date.format(DATE);
    }

    public static String plural(long count, String singular, String plural) {
        return count + " " + (count == 1 ? singular : plural);
    }
}
