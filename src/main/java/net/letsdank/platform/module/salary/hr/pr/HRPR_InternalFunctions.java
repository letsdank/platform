package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.HRPR_QueryDescriptionOperator;

// Служебные процедуры и функции
public class HRPR_InternalFunctions {
    // Alias: ДобавитьГруппировку
    public static void addGroup(HRPR_QueryDescriptionOperator operator, String groupExpression) {
        operator.getGroups().add(groupExpression);
    }
}
