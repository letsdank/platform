package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.HRPR_SQLQueryBuild;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;
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
    public void putFilter(Object filter, HRPR_FilterUsageDescription filterUsageDescription, TTRegistryNameBuildContext buildContext) {
        List<String> receiverJoinCondition = null;

        if (filterUsageDescription.isFilterAsTT()) {
            HRPR_QueryDescriptionJoin joinDescription = filterUsageDescription.getJoinDescriptionWithTableFilter();

            if (joinDescription != null) {
                receiverJoinCondition = joinDescription.getConditions();

                if (filterUsageDescription.isAllRecords() && joinDescription.getLeadingTable().equals(filterUsageDescription.getFilterTableAlias())) {
                    joinDescription.setJoinType("LEFT");
                }
            }

            String filterTTName = initFilterTT(filter, filterUsageDescription);
            filterUsageDescription.getQueryOperator().replaceTable(filterUsageDescription.getFilterTableAlias(), filterTTName);
        } else {
            filterUsageDescription.getQueryOperator().deleteTable(filterUsageDescription.getFilterTableAlias());

            for (HRPR_PeriodFieldDescription periodFieldDescription : filterUsageDescription.getCalculatedPeriodParametersDescription()) {
                String periodField = HRPR_FilterUsageDescription.getFilterTableField(filter, periodFieldDescription.getSourceFieldName());
                LocalDateTime value = periodFieldDescription.getFieldValue((LocalDateTime) filter.getFilterValue().getPeriodDescription().get(periodField));
                parameters.put(periodFieldDescription.getFieldName(), value);
            }

            for (Map.Entry<String, String> additionalField : filterUsageDescription.getAdditionalFields().entrySet()) {
                parameters.put(additionalField.getValue(), filter.getFilterValue().getAdditionalFields().get(additionalField.getKey()));
            }

            if (filterUsageDescription.getRegistryTableAlias() != null && !filterUsageDescription.getRegistryTableAlias().isEmpty()) {
                HRPR_QueryDescriptionJoin joinDescription = filterUsageDescription.getQueryOperator().getJoinDescriptionByJoiningTable(filterUsageDescription.getRegistryTableAlias());

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
    private void putFilterTTInitQueryDescription(Object filter, Map<String, String> filterPeriodFields, String filterTTName, String parameterNamePostfix) {
        TableMap filterTable;
        if (filter.getFilterValue().getType() == HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_MAP) {
            filterTable = filter.getFilterValue().getMap();
        } else {
            filterTable = filterListIntoTable(filter, filterPeriodFields);
        }

        List<String> tableFields = getFilterTableAllFields(filter, filterPeriodFields);

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

    // TODO: Перенести в класс фильтра с применением ООП
    public List<String> getFilterTableAllFields(Object filter) {
        return getFilterTableAllFields(filter, null);
    }

    // TODO: Перенести в класс фильтра с применением ООП
    public List<String> getFilterTableAllFields(Object filter, Map<String, String> periodFields) {
        List<String> fields = new ArrayList<>();

        for (Object dimension : filter.getFilterDimensions()) {
            String filterField = HRPR_FilterUsageDescription.getFilterTableField(filter, dimension);
            fields.add(filterField);
        }

        for (Object field : filter.getAdditionalFilterFields()) {
            String filterField = HRPR_FilterUsageDescription.getFilterTableField(filter, field);
            fields.add(filterField);
        }

        if (periodFields != null) {
            fields.addAll(periodFields.values());
        }

        return fields;
    }

    // Alias: ДополнитьОписаниеЗапросаИнициализацииВТФильтр
    private void appendFilterTTInitQueryDescription(HRPR_QueryDescription initQueryDescription, Map<String, String> filterPeriodFields) {
        for (String field : filterPeriodFields.values()) {
            initQueryDescription.addField(0, field, field, false);
        }
    }

    // Alias: ФильтрСписокЗначенийВТаблицуЗначений
    // TODO: Оооочень спорная функция - надо понять, действительно она нужна или нет
    private TableMap filterListIntoTable(Object filter, Map<String, String> filterPeriodFields) {
        TableMap filterTable = new TableMap();
        if (filter.getFilterDimensions().size() == 1) {
            List<Class<?>> filterTypes = new ArrayList<>();

            for (Object value : filter.getFilterValues().getDimensionValues()) {
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

            filterTable.loadColumn(filter.getFilterValues().getDimensionValues(), filter.getFilterDimensions().get(0));
        } else {
            filterTable.addRow();
        }

        for (Map.Entry<String, LocalDateTime> periodField : filter.getFilterValues().getPeriodDescription()) {
            if (periodField.getValue() != null) {
                filterTable.addColumn(periodField.getKey(), LocalDateTime.class);
                filterTable.fillValues(periodField.getValue(), periodField.getKey());
            }
        }

        for (Map.Entry<String, Object> additionalField : filter.getFilterValues().getAdditionalFields()) {
            if (additionalField.getValue() != null) {
                filterTable.addColumn(additionalField.getKey(), List.class);
                filterTable.fillValues(additionalField.getValue(), additionalField.getKey());
            }
        }

        return filterTable;
    }
}
