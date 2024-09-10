package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HRPR_QueryDescriptionNestedQuery {
    // Alias: НовыйОписаниеВложенногоЗапроса
    private String alias;
    private String type;
    private HRPR_QueryDescription queryDescription;
}
