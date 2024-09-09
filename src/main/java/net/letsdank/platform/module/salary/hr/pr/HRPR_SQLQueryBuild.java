package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.HRPR_QueryDescription;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchema;

// Построение модели запроса
public class HRPR_SQLQueryBuild {
    public static HRPR_QueryDescription getQueryDescriptionByText(String text) {
        QuerySchema schema = new QuerySchema();
        schema.setQuery(text);

        return getQueryDescriptionBySchemaPacket(schema.getPackets().get(0));
    }
}
