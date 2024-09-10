package net.letsdank.platform.module.salary.hr.pr.period;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.date.LDTUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class HRPR_PeriodFieldDescription {
    private String tableAlias;
    private String fieldName;
    private HRPR_PeriodMultiplicity multiplicity = HRPR_PeriodMultiplicity.SECOND;
    private int offset = 0;
    private HRPR_CastPeriodOption castPeriodOption = HRPR_CastPeriodOption.STARTPERIOD;
    private HRPR_PeriodMultiplicity offsetMultiplicity;
    private boolean emptyValueAsMaximum = false;
    private String sourceFieldName;

    // Alias: ОписаниеПоляПериода
    public HRPR_PeriodFieldDescription(String fieldName) {
        this(fieldName, null);
    }

    // Alias: ОписаниеПоляПериода
    public HRPR_PeriodFieldDescription(String fieldName, String tableAlias) {
        this.fieldName = fieldName;
        this.tableAlias = tableAlias;
    }

    // Alias: ВыражениеПоляПериод
    public String getPeriodFieldExpression() {
        if (tableAlias == null) {
            return "&" + fieldName;
        }

        String periodExpression;
        String periodExpressionTemplate = null;
        if (emptyValueAsMaximum) {
            periodExpressionTemplate = "CASE WHEN &date_ = NULL THEN '3999-12-31 23:59:59'" +
                    "  ELSE &date_given_" +
                    "  END";

            String dateExpression = tableAlias + "." + fieldName;
            periodExpressionTemplate = periodExpressionTemplate.replace("&date_", dateExpression);

            periodExpression = dateExpression;
        } else {
            periodExpression = tableAlias + "." + fieldName;
        }

        if (multiplicity != HRPR_PeriodMultiplicity.SECOND) {
            periodExpression = castPeriodOption + "(" + periodExpression + "," + multiplicity.name() + ")"; // TODO Это поддерживается силами postgres?
        }

        HRPR_PeriodMultiplicity offsetMultiplicity = this.offsetMultiplicity == null ? multiplicity : this.offsetMultiplicity;

        if (offset != 0) {
            periodExpression = "ADDTODATE(" + periodExpression + "," + offsetMultiplicity.name() + "," + offset + ")";
        }

        if (emptyValueAsMaximum) {
            return periodExpressionTemplate.replace("&date_given_", periodExpression);
        }

        return periodExpression;
    }

    // Alias: ЗначениеПоляПериод
    public LocalDateTime getFieldValue(LocalDateTime sourceValue) {
        if (emptyValueAsMaximum && sourceValue == null) {
            return LocalDateTime.of(3999, 12, 31, 23, 59, 59);
        }

        LocalDateTime calculatedLdt = convertPeriodField(sourceValue, castPeriodOption, multiplicity);
        HRPR_PeriodMultiplicity offsetMultiplicity = this.offsetMultiplicity == null ?
                multiplicity : this.offsetMultiplicity;

        return convertPeriodFieldOffset(calculatedLdt, offsetMultiplicity, offset);
    }

    // Alias: ЗначениеПоляПериодПриведенное
    private LocalDateTime convertPeriodField(LocalDateTime sourceValue, HRPR_CastPeriodOption castPeriodOption, HRPR_PeriodMultiplicity multiplicity) {
        return switch (multiplicity) {
            case MINUTE -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfMinute(sourceValue) :
                    LDTUtils.getEndOfMinute(sourceValue);

            case HOUR -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfHour(sourceValue) :
                    LDTUtils.getEndOfHour(sourceValue);

            case DAY -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfDay(sourceValue) :
                    LDTUtils.getEndOfDay(sourceValue);

            case WEEK -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfWeek(sourceValue) :
                    LDTUtils.getEndOfWeek(sourceValue);

            case MONTH -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfMonth(sourceValue) :
                    LDTUtils.getEndOfMonth(sourceValue);

            case YEAR -> castPeriodOption == HRPR_CastPeriodOption.STARTPERIOD ?
                    LDTUtils.getStartOfYear(sourceValue) :
                    LDTUtils.getEndOfYear(sourceValue);

            default -> sourceValue;
        };
    }

    // Alias: ЗначениеПоляПериодСдвинутое
    private LocalDateTime convertPeriodFieldOffset(LocalDateTime sourceValue, HRPR_PeriodMultiplicity multiplicity, int offset) {
        return switch (multiplicity) {
            case SECOND -> sourceValue.plusSeconds(offset);
            case MINUTE -> sourceValue.plusMinutes(offset);
            case HOUR -> sourceValue.plusHours(offset);
            case DAY -> sourceValue.plusDays(offset);
            case WEEK -> sourceValue.plusWeeks(offset);
            case MONTH -> sourceValue.plusMonths(offset);
            case QUARTER -> sourceValue.plusMonths(offset * 3L);
            case YEAR -> sourceValue.plusYears(offset);
            default -> sourceValue;
        };
    }
}
