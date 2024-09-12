package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;

// TODO: Переименовать
@Getter
@Setter
public class HRPR_Filter {
    private String leftValue;
    private Object rightValue;
    private Boolean relativePath;
    private String comparison;
}
