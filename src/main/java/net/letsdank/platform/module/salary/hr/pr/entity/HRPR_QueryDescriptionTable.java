package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HRPR_QueryDescriptionTable {
    private String name;
    private String alias;
    private String type;

    private HRPR_QueryDescription queryDescription;
}
