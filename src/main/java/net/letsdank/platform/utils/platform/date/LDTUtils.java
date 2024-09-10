package net.letsdank.platform.utils.platform.date;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// LocalDateTimeUtils
public class LDTUtils {
    public static LocalDateTime getStartOfMinute(LocalDateTime ldt) {
        return ldt.truncatedTo(ChronoUnit.MINUTES);
    }

    public static LocalDateTime getEndOfMinute(LocalDateTime ldt) {
        return ldt.plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfHour(LocalDateTime ldt) {
        return ldt.truncatedTo(ChronoUnit.HOURS);
    }

    public static LocalDateTime getEndOfHour(LocalDateTime ldt) {
        return ldt.plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfDay(LocalDateTime ldt) {
        return ldt.truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getEndOfDay(LocalDateTime ldt) {
        return ldt.plusHours(23).plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfWeek(LocalDateTime ldt) {
        return ldt.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getEndOfWeek(LocalDateTime ldt) {
        return ldt.with(DayOfWeek.SUNDAY).plusHours(23).plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfMonth(LocalDateTime ldt) {
        return ldt.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getEndOfMonth(LocalDateTime ldt) {
        return ldt.withDayOfMonth(ldt.getMonth().maxLength())
                .plusHours(23).plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfQuarter(LocalDateTime ldt) {
        int quarter = (ldt.getMonthValue() - 1) / 3 + 1;
        int startMonth = (quarter - 1) * 3 + 1;
        return ldt.withMonth(startMonth).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getEndOfQuarter(LocalDateTime ldt) {
        int quarter = (ldt.getMonthValue() - 1) / 3 + 1;
        int endMonth = quarter * 3;
        return ldt.withMonth(endMonth).withDayOfMonth(ldt.getMonth().maxLength())
                .plusHours(23).plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime getStartOfYear(LocalDateTime ldt) {
        return ldt.withMonth(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getEndOfYear(LocalDateTime ldt) {
        return ldt.withMonth(12).withDayOfMonth(31).plusHours(23)
                .plusMinutes(59).plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    }
}
