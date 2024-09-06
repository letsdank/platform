package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.RegistryQueriesDescriptionPacket;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import net.letsdank.platform.utils.platform.sql.description.QueryDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Работа с моделью запроса
public class HRPR_SQLQuery {
    // Alias: ЗапросПоОписаниюПакета
    public static SQLQuery getQueryByDescriptionPacket(RegistryQueriesDescriptionPacket packet, boolean showReportElements) {
        SQLQuery query = new SQLQuery();

        for (Map.Entry<String, Object> entry : packet.getParameters().entrySet()) {
            query.addParameter(entry.getKey(), entry.getValue());
        }

        String delimiter = "\n;\n";
        List<String> queriesPacket = new ArrayList<>();

        if (packet.getInitFiltersQueryDescription() != null) {
            HRPR_SQLGeneration.performQueryDescriptionToStrings(queriesPacket, packet.getInitFiltersQueryDescription(), showReportElements);
            queriesPacket.add(delimiter);
        }

        int queryIndex = 0;
        for (Either<SQLQuery, QueryDescription> queryDescription : packet.getDataQueries()) {
            queryIndex++;
            if (queryDescription.isLeft()) {
                queriesPacket.add(queryDescription.getLeft().getSql());
            } else {
                HRPR_SQLGeneration.performQueryDescriptionToStrings(queriesPacket, queryDescription.getRight(), showReportElements);
            }

            if (queryIndex != packet.getDataQueries().size()) {
                queriesPacket.add(delimiter);
            }
        }

        if (packet.getDestroyFilterQuery() != null) {
            queriesPacket.add(delimiter);
            queriesPacket.add(packet.getDestroyFilterQuery().getSql());
        }

        query.setSql(String.join("", queriesPacket));
        return query;
    }
}
