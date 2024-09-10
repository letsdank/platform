package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HRPR_QueryDescriptionNestedQuery {
    private String alias;
    private String type;
    private HRPR_QueryDescription queryDescription;
}
