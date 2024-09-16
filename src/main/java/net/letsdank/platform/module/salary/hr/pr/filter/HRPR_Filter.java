package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;

// TODO: Переименовать
// TODO: Это разве не HRBD_FilterDescription?
@Getter
@Setter
public class HRPR_Filter {
    private String leftValue;
    private Object rightValue;
    private Boolean relativePath;
    private String comparison;

    // Alias: ИмяПараметраЭлементаФильтра
    public static String getParameterNameByFilterElement(String parameterName, String tableName) {
        return parameterName + "_" + tableName;
    }
}
