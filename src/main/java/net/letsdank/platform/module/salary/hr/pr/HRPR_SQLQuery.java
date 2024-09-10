package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.description.*;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.SQLQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Работа с моделью запроса
public class HRPR_SQLQuery {
    // Alias: ЗапросПоОписаниюПакета
    public static SQLQuery getQueryByDescriptionPacket(HRPR_RegistryQueriesDescriptionPacket packet, boolean showCompositionElements) {
        SQLQuery query = new SQLQuery();

        for (Map.Entry<String, Object> entry : packet.getParameters().entrySet()) {
            query.addParameter(entry.getKey(), entry.getValue());
        }

        String delimiter = "\n;\n";
        List<String> queriesPacket = new ArrayList<>();

        if (packet.getInitFiltersQueryDescription() != null) {
            HRPR_SQLGeneration.performQueryDescriptionToStrings(queriesPacket, packet.getInitFiltersQueryDescription(), showCompositionElements);
            queriesPacket.add(delimiter);
        }

        int queryIndex = 0;
        for (Either<SQLQuery, HRPR_QueryDescription> queryDescription : packet.getDataQueries()) {
            queryIndex++;
            if (queryDescription.isLeft()) {
                queriesPacket.add(queryDescription.left().getSql());
            } else {
                HRPR_SQLGeneration.performQueryDescriptionToStrings(queriesPacket, queryDescription.right(), showCompositionElements);
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

    // Alias: ЗаменитьТаблицуВОператореЗапроса
    public static void replaceTableInQueryOperator(HRPR_QueryDescriptionOperator operator, String replacementTableAlias, String newTableName) {
        Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery> tableDescription = operator.getSources().get(replacementTableAlias);
        if (tableDescription.isRight()) {
            tableDescription.right().setName(newTableName);
        }
    }

    // Alias: ДобавитьПолеВОписаниеЗапроса
    public static void addFieldToQueryDescription(HRPR_QueryDescription queryDescription, int operatorDescriptionIndex, String expression,
                                                  String alias) {
        addFieldToQueryDescription(queryDescription, operatorDescriptionIndex, expression, alias, true);
    }

    // Alias: ДобавитьПолеВОписаниеЗапроса
    public static void addFieldToQueryDescription(HRPR_QueryDescription queryDescription, int queryOperatorIndex, String expression,
                                                  String alias, boolean addFieldToComposition) {
        int fieldIndex = queryDescription.getColumns().indexOf(alias);

        if (fieldIndex == -1) {
            queryDescription.getColumns().add(alias);
            int queryIndex = 0;
            for (HRPR_QueryDescriptionOperator query : queryDescription.getOperators()) {
                queryDescription.getOperators().get(queryIndex).getSelectedFields().add
                        (queryIndex == queryOperatorIndex ? expression : "NULL");

                query.getFieldAliases().add(alias);
                queryIndex++;
            }
        } else {
            queryDescription.getOperators().get(queryOperatorIndex).getSelectedFields().set(fieldIndex, expression);
        }

        if (addFieldToComposition && queryOperatorIndex == 0) {
            queryDescription.getOperators().get(queryOperatorIndex).getSelectedCompositionFields().add(
                    new HRPR_QueryDescriptionCompositionField(alias, null));
        }
    }

    // Alias: ДобавитьУсловие
    public static void addCondition(HRPR_QueryDescriptionOperator operator, String conditions) {
        operator.getConditions().add(conditions);
    }

    // Alias: ДобавитьУсловие
    public static void addCondition(HRPR_QueryDescriptionOperator operator, List<String> conditions) {
        for (String condition : conditions) {
            operator.getConditions().add(condition);
        }
    }
}
