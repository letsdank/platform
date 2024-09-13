package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.TableMap;

@Getter
@Setter
public class HRPR_FilterValueTableMap extends HRPR_FilterValue {
    private TableMap tableMap;

    // Alias: ФильтрТаблицаЗначений
    public HRPR_FilterValueTableMap(TableMap tableMap) {
        super(HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_MAP);
        this.tableMap = tableMap;
    }
}
