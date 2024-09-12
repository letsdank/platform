package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;

// Служебные процедуры и функции
public class HRPR_InternalFunctions {
    // Alias: ВыражениеДопПоляФильтраПоОписаниюИспользованияФильтра
    public static String getAdditionalFieldExpressionByFUD(String additionalFieldName, HRPR_FilterUsageDescription fud) {
        return (fud.isFilterAsTT() ? "" : "&") + fud.getAdditionalFields().get(additionalFieldName);
    }
}
