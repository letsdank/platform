package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.sql.TempTableManager;

@Getter
@Setter
public class HRPR_FilterValueTempTable extends HRPR_FilterValue {
    private String ttName;
    private TempTableManager ttManager;

    // Alias: ФильтрВременнаяТаблица
    public HRPR_FilterValueTempTable(String ttName, TempTableManager ttManager) {
        super(HRPR_FilterUsageDescription.TYPE_FILTER_TEMP_TABLE);
        this.ttName = ttName;
        this.ttManager = ttManager;
    }
}
