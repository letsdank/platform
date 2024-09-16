package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HRPR_FilterValue {
    private String type;

    protected HRPR_FilterValue(String type) {
        this.type = type;
    }

    // Alias: ИмяПараметраЭлементаФильтра
    public static String getParameterNameByFilterElement(String parameterName, String tableName) {
        return parameterName + "_" + tableName;
    }
}
