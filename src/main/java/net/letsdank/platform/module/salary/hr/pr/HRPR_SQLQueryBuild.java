package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.*;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.*;
import net.letsdank.platform.utils.platform.sql.schema.join.QuerySchemaQuerySourceJoin;
import net.letsdank.platform.utils.platform.sql.schema.order.QuerySchemaOrderDirection;
import net.letsdank.platform.utils.platform.sql.schema.order.QuerySchemaOrderExpression;
import net.letsdank.platform.utils.platform.sql.schema.query.QuerySchemaSelectQuery;

import java.util.ArrayList;
import java.util.List;

// Построение модели запроса
public class HRPR_SQLQueryBuild {
    // Alias: ОписаниеЗапросаПоТексту
    public static HRPR_QueryDescription getQueryDescriptionByText(String text) {
        QuerySchema schema = new QuerySchema();
        schema.setQuery(text);

        return getQueryDescriptionBySchemaPacket(schema.getPackets().get(0).left());
    }

    /**
     * Returns query description
     * @param packet QuerySchemaSelectQuery
     * @return HRPR_QueryDescription
     */
    // Alias: ОписаниеЗапросаПоЗапросуПакета
    static HRPR_QueryDescription getQueryDescriptionBySchemaPacket(QuerySchemaSelectQuery packet) {
        HRPR_QueryDescription description = new HRPR_QueryDescription();

        description.setDistinct(packet.isSelectAllowed());
        description.setTableToPlace(packet.getPlacementTable());

        for (QuerySchemaOrderExpression orderExpression : packet.getOrder()) {
            String orderElement;
            if (orderExpression.getItem().isRight()) {
                orderElement = orderExpression.getItem().right().getAlias();
            } else {
                orderElement = orderExpression.getItem().toString();
            }

            if (!isTemplateExpression(orderElement)) {
                description.getOrder().add(orderElement + (orderExpression.getDirection() == QuerySchemaOrderDirection.DESCENDING ? " DESC" : ""));
            }
        }

        for (QuerySchemaSelectOperator operator : packet.getOperators()) {
            HRPR_QueryDescriptionOperator queryDescriptionOperator = new HRPR_QueryDescriptionOperator();
            description.getOperators().add(queryDescriptionOperator);

            queryDescriptionOperator.setJoinType(HRPR_SQLGeneration.getUnionTypeAsString(operator.getUnionType()));
            queryDescriptionOperator.setDistinct(operator.isSelectDistinct());
            queryDescriptionOperator.setRecordCount(operator.getRetrievedRecordsCount());

            for (QuerySchemaExpression groupExpression : operator.getGroup()) {
                String groupElement = groupExpression.toString();
                if (!isTemplateExpression(groupElement)) {
                    HRPR_InternalFunctions.addGroup(queryDescriptionOperator, groupElement);
                }
            }

            for (QuerySchemaExpression filterExpression : operator.getFilter()) {
                String filterElement = filterExpression.toString();
                if (!isTemplateExpression(filterElement)) {
                    if (filterExpression.containsAggregationFunction()) {
                        queryDescriptionOperator.getHaving().add(filterElement);
                    } else {
                        HRPR_SQLQuery.addCondition(queryDescriptionOperator, filterElement);
                    }
                }
            }

            for (QuerySchemaSource schemaSource : operator.getSources()) {
                Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery> queryTableDescription;
                String sourceAlias = schemaSource.getSourceAlias();

                if (schemaSource.getSource().isMiddle()) {
                    queryTableDescription = Either.left(getNestedQueryDescription(schemaSource.getSource().middle().getQuery(), sourceAlias));
                } else if (schemaSource.getSource().isLeft()) {
                    queryTableDescription = Either.right(getQueryTableDescription(schemaSource.getSource().left().getTableName(), sourceAlias));
                } else {
                    queryTableDescription = Either.right(getQueryTableDescription(schemaSource.getSource().right().getTableName(), sourceAlias));
                }

                queryDescriptionOperator.getTables().add(queryTableDescription);
                queryDescriptionOperator.getSources().put(sourceAlias, queryTableDescription);

                int joinOrder = 0;
                for (QuerySchemaQuerySourceJoin join : schemaSource.getJoins()) {
                    List<HRPR_QueryDescriptionJoin> joinRows = queryDescriptionOperator.getJoins().stream()
                            .filter(j -> j.getJoiningTable().equals(join.getSource().getSourceAlias()))
                            .toList();
                    HRPR_QueryDescriptionJoin joinDescription;

                    if (joinRows.isEmpty()) {
                        joinOrder++;

                        joinDescription = new HRPR_QueryDescriptionJoin();
                        joinDescription.setLeadingTable(schemaSource.getSourceAlias());
                        joinDescription.setJoiningTable(join.getSource().getSourceAlias());
                        joinDescription.setJoinType(join.getJoinType().toString());
                        joinDescription.setConditions(new ArrayList<>());
                        joinDescription.setJoinOrder(joinOrder);

                        queryDescriptionOperator.getJoins().add(joinDescription);
                    } else {
                        joinDescription = joinRows.get(0);
                    }

                    String conditionRow = "(" + join.getCondition().toString() + ")";
                    if (!isTemplateExpression(conditionRow)) {
                        joinDescription.getConditions().add(conditionRow);
                    }
                }
            }
        }

        int fieldOrder = 0;
        for (Either<QuerySchemaColumn, QuerySchemaNestedTableColumn> column : packet.getColumns()) {
            List<Either<QuerySchemaExpression, QuerySchemaNestedTable>> columnFields = column.isRight() ? column.right().getFields() : column.left().getFields();
            String columnAlias = column.isRight() ? column.right().getAlias() : column.left().getAlias();

            List<String> addingFields = new ArrayList<>();
            boolean addColumn = false;

            for (Either<QuerySchemaExpression, QuerySchemaNestedTable> field : columnFields) {
                String fieldAsString = field.toString();
                if (isTemplateExpression(fieldAsString)) {
                    addingFields.add("NULL");
                } else {
                    addingFields.add(fieldAsString);
                    addColumn = true;
                }
            }

            if (addColumn) {
                for (int opIndex = 0; opIndex < addingFields.size(); opIndex++) {
                    HRPR_SQLQuery.addFieldToQueryDescription(description, opIndex, addingFields.get(opIndex), columnAlias, false);
                }
            }
        }

        return description;
    }

    // Alias: ОписаниеТаблицыЗапроса
    static HRPR_QueryDescriptionTableQuery getQueryTableDescription(String name, String alias) {
        HRPR_QueryDescriptionTableQuery description = new HRPR_QueryDescriptionTableQuery();
        description.setName(name);
        description.setAlias(alias);
        description.setType("TableQueryDescription");

        return description;
    }

    // Alias: ОписаниеВложенногоЗапроса
    static HRPR_QueryDescriptionNestedQuery getNestedQueryDescription(QuerySchemaSelectQuery packetQuery, String alias) {
        HRPR_QueryDescriptionNestedQuery description = new HRPR_QueryDescriptionNestedQuery();
        description.setQueryDescription(getQueryDescriptionBySchemaPacket(packetQuery));
        description.setAlias(alias);
        description.setType("NestedQueryDescription");

        return description;
    }

    // Alias: ЭтоВыражениеШаблон
    static boolean isTemplateExpression(String expression) {
        return expression.startsWith("&tmpl") || expression.startsWith("(&tmpl");
    }
}
