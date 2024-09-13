package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.HRPR_SQLQueryBuild;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.filter.CreateTTRegistryNameFilter;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterValueList;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterValueTableMap;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodFieldDescription;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.data.TableMap;
import net.letsdank.platform.utils.platform.sql.SQLQuery;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class HRPR_RegistryQueriesDescriptionPacket {
    private HRPR_QueryDescription initFiltersQueryDescription;
    private SQLQuery destroyFilterQuery;
    private List<Either<SQLQuery, HRPR_QueryDescription>> dataQueries;
    private Map<String, Object> parameters;

    // Alias: НовыйОписаниеПакетаЗапросовКРегистру
    public HRPR_RegistryQueriesDescriptionPacket() {
        dataQueries = new ArrayList<>();
        parameters = new HashMap<>();
    }

    // Alias: УстановитьФильтрВОписаниеПакетаЗапросовКРегистру
    public void putFilter(CreateTTRegistryNameFilter<HRPR_FilterValueList> filter, HRPR_FilterUsageDescription filterUsageDescription,
                          TTRegistryNameBuildContext buildContext) {
        List<String> receiverJoinConditions = null;

        if (filterUsageDescription.isFilterAsTT()) {
            HRPR_QueryDescriptionJoin joinDescription = filterUsageDescription.getJoinDescriptionWithTableFilter();

            if (joinDescription != null) {
                receiverJoinConditions = joinDescription.getConditions();

                if (filterUsageDescription.isAllRecords() && joinDescription.getLeadingTable().equals(filterUsageDescription.getFilterTableAlias())) {
                    joinDescription.setJoinType("LEFT");
                }
            }

            String filterTTName = initFilterTT(filter, filterUsageDescription);
            filterUsageDescription.getQueryOperator().replaceTable(filterUsageDescription.getFilterTableAlias(), filterTTName);
        } else {
            filterUsageDescription.getQueryOperator().deleteTable(filterUsageDescription.getFilterTableAlias());

            for (HRPR_PeriodFieldDescription periodFieldDescription : filterUsageDescription.getCalculatedPeriodParametersDescription()) {
                String periodField = filter.getFilterTableField(periodFieldDescription.getSourceFieldName());
                LocalDateTime value = periodFieldDescription.getFieldValue((LocalDateTime) filter.getFilterValue().getPeriodDescription().get(periodField));
                parameters.put(periodFieldDescription.getFieldName(), value);
            }

            for (Map.Entry<String, String> additionalField : filterUsageDescription.getAdditionalFields().entrySet()) {
                parameters.put(additionalField.getValue(), filter.getFilterValue().getAdditionalFields().get(additionalField.getKey()));
            }

            if (filterUsageDescription.getRegistryTableAlias() != null && !filterUsageDescription.getRegistryTableAlias().isEmpty()) {
                HRPR_QueryDescriptionJoin joinDescription = filterUsageDescription.getQueryOperator().getJoinDescriptionByJoiningTable(filterUsageDescription.getRegistryTableAlias());

                if (joinDescription != null) {
                    receiverJoinConditions = joinDescription.getConditions();
                } else {
                    receiverJoinConditions = filterUsageDescription.getQueryOperator().getConditions();
                }
            }
        }

        putDimensionFilterParameters(filter, filterUsageDescription);

        if (receiverJoinConditions != null) {
            putJoinConditionsWithFilter(filterUsageDescription, receiverJoinConditions, filter);
        }

        putConditionsComposition(filterUsageDescription, buildContext);
    }

    // Alias: УстановитьПараметрыИзмеренияФильтра
    private void putDimensionFilterParameters(Object filter, HRPR_FilterUsageDescription filterUsageDescription) {
        if (filterUsageDescription.isFilterAsTT()) {
            for (String filterDimensionKey : filterUsageDescription.getFilterDimensions().keySet()) {
                String filterFieldExpression = filterUsageDescription.getFilterDimensionExpression(filterDimensionKey);
                String parameterName = filterFieldExpression.replace("&", "");
                Object parameterValue;

                if (filterUsageDescription.isAllRecords()) {
                    parameterValue = filter.getFilterValue().getDimensionValues().empty() ? null :
                            filter.getFilterValue().getDimensionValues().get(0);
                } else {
                    parameterValue = filter.getFilterValue().getDimensionValues();
                }

                parameters.put(parameterName, parameterValue);
            }
        }
    }

    // Alias: УстановитьЭлементыОтбораСКД
    private void putConditionsComposition(HRPR_FilterUsageDescription filterUsageDescription) {
        putConditionsComposition(filterUsageDescription, null);
    }


    // Alias: УстановитьЭлементыОтбораСКД
    private void putConditionsComposition(HRPR_FilterUsageDescription filterUsageDescription, TTRegistryNameBuildContext buildContext) {
        if (filterUsageDescription.getRegistryTableAlias() == null || filterUsageDescription.getRegistryTableAlias().isEmpty()) {
            return;
        }

        if (filterUsageDescription.isFilterAsTT()) {
            for (String filterDimensionKey : filterUsageDescription.getFilterDimensions().keySet()) {
                String conditionAlias = null;
                if (buildContext != null) {
                    conditionAlias = buildContext.getFieldAliasCorrespondenceForComposition().get(filterDimensionKey);
                }

                String filterFieldExpression = filterUsageDescription.getRegistryTableAlias() + "." + filterDimensionKey + ".*";
                if (conditionAlias != null) {
                    filterFieldExpression += " AS " + conditionAlias;
                    filterUsageDescription.getQueryOperator().getCompositionFieldConditions().add(filterFieldExpression);
                }
            }
        }
    }

    // Alias: УстановитьУсловияСвязиСФильтром
    private void putJoinConditionsWithFilter(HRPR_FilterUsageDescription filterUsageDescription, List<String> receiverJoinConditions, Object filter) {
        if (filterUsageDescription.getTemplateJoinCondition() != null && !filterUsageDescription.getTemplateJoinCondition().isEmpty()) {
            receiverJoinConditions.add(filterUsageDescription.getTemplateJoinCondition());
        }

        for (String filterDimensionKey : filterUsageDescription.getFilterDimensions().keySet()) {
            String filterFieldExpression = filterUsageDescription.getFilterDimensionExpression(filterDimensionKey);
            if (filterUsageDescription.isFilterAsTT()) {
                if (receiverJoinConditions != null) {
                    String queryCondition = filterUsageDescription.getRegistryTableAlias() + "." + filterDimensionKey +
                            " = " + filterFieldExpression;
                    receiverJoinConditions.add(queryCondition);
                }
            } else {
                String parameterName = filterFieldExpression.replace("&", "");

                if (receiverJoinConditions != null) {
                    String queryCondition;
                    if (filterUsageDescription.isAllRecords()) {
                        queryCondition = filterUsageDescription.getRegistryTableAlias() + "." + filterDimensionKey +
                                " = " + filterFieldExpression;
                    } else {
                        queryCondition = filterUsageDescription.getRegistryTableAlias() + "." + filterDimensionKey +
                                " IN (" + filterFieldExpression + ")";
                    }

                    receiverJoinConditions.add(queryCondition);
                }
            }
        }
    }

    // Alias: ИнициализироватьВТФильтр
    private String initFilterTT(Object filter, HRPR_FilterUsageDescription filterUsageDescription) {
        if (filter.getFilterValue().getType() == HRPR_FilterUsageDescription.TYPE_FILTER_TEMP_TABLE) {
            return filter.getFilterValue().getTtName();
        } else if (initFiltersQueryDescription == null) {
            String filterTTName = "vt_filter_for_" + filterUsageDescription.getParameterNamePostfix();
            putFilterTTInitQueryDescription(filter, filterUsageDescription.getFilterPeriodFields(), filterTTName, filterUsageDescription.getParameterNamePostfix());

            String queryDelete = "DROP TABLE " + filterTTName;
            this.destroyFilterQuery = new SQLQuery(queryDelete);

            return filterTTName;
        } else {
            appendFilterTTInitQueryDescription(initFiltersQueryDescription, filterUsageDescription.getFilterPeriodFields());
            return getTTFilterMapName();
        }
    }

    // Alias: УстановитьОписаниеЗапросаИнициализацииВТФильтр
    private void putFilterTTInitQueryDescription(CreateTTRegistryNameFilter<?> filter, Map<String, String> filterPeriodFields, String filterTTName, String parameterNamePostfix) {
        TableMap filterTable;
        if (filter.getFilterValue().getType() == HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_MAP) {
            filterTable = ((HRPR_FilterValueTableMap) filter.getFilterValue()).getTableMap();
        } else {
            filterTable = filterListIntoTable(filter, filterPeriodFields);
        }

        List<String> tableFields = filter.getFilterTableAllFields(filterPeriodFields);

        String queryTemplate = " SELECT INTO vt_filter " +
                "  &tmpl_filter_field AS tmpl_filter_field " +
                "  FROM &filter_table AS filter_table";

        HRPR_QueryDescription initQueryDescription = HRPR_SQLQueryBuild.getQueryDescriptionByText(queryTemplate);
        initQueryDescription.setTableToPlace(filterTTName);

        String tableParameterName = "filter_table_" + parameterNamePostfix;
        initQueryDescription.getOperators().get(0).replaceTable("filter_table_", "&" + tableParameterName);

        for (String field : tableFields) {
            initQueryDescription.addField(0, "filter_table." + field, field, false);
        }

        parameters.put(tableParameterName, filterTable);
        initFiltersQueryDescription = initQueryDescription;
    }

    // Alias: ДополнитьОписаниеЗапросаИнициализацииВТФильтр
    private void appendFilterTTInitQueryDescription(HRPR_QueryDescription initQueryDescription, Map<String, String> filterPeriodFields) {
        for (String field : filterPeriodFields.values()) {
            initQueryDescription.addField(0, field, field, false);
        }
    }

    // Alias: ФильтрСписокЗначенийВТаблицуЗначений
    // TODO: Оооочень спорная функция - надо понять, действительно она нужна или нет
    private TableMap filterListIntoTable(CreateTTRegistryNameFilter<HRPR_FilterValueList> filter, Map<String, String> filterPeriodFields) {
        TableMap filterTable = new TableMap();
        if (filter.getFilterDimensions().size() == 1) {
            List<Class<?>> filterTypes = new ArrayList<>();

            for (Object value : filter.getFilterValue().getDimensionValues()) {
                if (!filterTypes.contains(value.getClass())) {
                    filterTypes.add(value.getClass());
                }
                filterTable.addRow();
            }

            if (!filterTypes.isEmpty()) {
                filterTable.addColumn(filter.getFilterDimensions().get(0), Map.class);
            } else {
                filterTable.addColumn(filter.getFilterDimensions().get(0), null);
            }

            filterTable.loadColumn(filter.getFilterValue().getDimensionValues(), filter.getFilterDimensions().get(0));
        } else {
            filterTable.addRow();
        }

        // TODO: Подумать
        for (Map.Entry<String, LocalDateTime> periodField : filter.getFilterValue().getPeriodDescription().entrySet()) {
            if (periodField.getValue() != null) {
                filterTable.addColumn(periodField.getKey(), LocalDateTime.class);
                filterTable.fillValues(periodField.getValue(), periodField.getKey());
            }
        }

        for (Map.Entry<String, Object> additionalField : filter.getFilterValue().getAdditionalFields().entrySet()) {
            if (additionalField.getValue() != null) {
                filterTable.addColumn(additionalField.getKey(), List.class);
                filterTable.fillValues(additionalField.getValue(), additionalField.getKey());
            }
        }

        return filterTable;
    }

    // Alias: ОписаниеЗапросаПакетаПоИмениВТ
    public HRPR_QueryDescription getPacketQueryDescriptionByTTName(String resultTTName) {
        for (Either<SQLQuery, HRPR_QueryDescription> packetQuery : dataQueries) {
            if (packetQuery.isRight() && packetQuery.right().getTableToPlace().equals(resultTTName)) {
                return packetQuery.right();
            }
        }

        return null;
    }

    // Alias: ОписаниеФильтраДляПолученияЗаписейНаНачалоПериода
    private Object getFilterDescriptionRecordsByPeriodStart(Object filterTransactions) {
        return getFilterDescriptionRecordsByPeriodStart(filterTransactions, false);
    }

    // Alias: ОписаниеФильтраДляПолученияЗаписейНаНачалоПериода
    private Object getFilterDescriptionRecordsByPeriodStart(Object filterTransactions, boolean allRecords) {
        Object result = null;

        if (HRPR_FilterUsageDescription.isUseTTFilter(filterTransactions, allRecords) &&
            filterTransactions.getFilterValue().getType() != HRPR_FilterUsageDescription.TYPE_FILTER_TEMP_TABLE) {
            String tempFilterTTName = getTTFilterMapName();

            // TODO: Implement
        }
    }

    // Alias: ИмяВТТаблицыЗначенийФильтра
    private String getTTFilterMapName() {
        return initFiltersQueryDescription != null ? initFiltersQueryDescription.getTableToPlace() : null;
    }

    // Alias: ДобавитьЗапросПолученияЗаписейНаНачалоПериода
    public HRPR_QueryDescriptionOperator addQueryRecordsByPeriodStart(boolean onlyDistrict, HRBD_RegistryDescription registryDescription, Object filter,
                                                                      TTRegistryNameBuildContext buildContext, String resultTTName) {
        // TODO: Implement
    }
}
