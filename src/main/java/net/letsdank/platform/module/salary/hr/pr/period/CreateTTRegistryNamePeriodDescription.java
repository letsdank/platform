package net.letsdank.platform.module.salary.hr.pr.period;

import java.util.HashMap;
import java.util.Map;

public class CreateTTRegistryNamePeriodDescription {
    // Поскольку в методе HRPR_FilterValueList.byTableMap используются динамические поля,
    // мы не можем типизировать и привести к нужному типу, поэтому остается единственный вариант
    // оставить этот класс как статику для создания данного объекта

    // Alias: ОписаниеПериодаДляСоздатьВТИмяРегистра
    public static Map<String, Object> create() {
        Map<String, Object> result = new HashMap<>();

        result.put("period", null);
        result.put("startDate", null);
        result.put("endDate", null);

        return result;
    }
}
