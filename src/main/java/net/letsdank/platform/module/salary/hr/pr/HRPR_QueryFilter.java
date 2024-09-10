package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.filter.FilterUsageDescription;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionJoin;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionOperator;
import net.letsdank.platform.utils.string.StringUtils;

import java.util.List;

// Работа с фильтром запроса
public class HRPR_QueryFilter {
    // Alias: ОписаниеИспользованиеФильтра
    public static FilterUsageDescription getFilterUsageDescription() {
        return getFilterUsageDescription("date_dimensions");
    }

    // Alias: ОписаниеИспользованиеФильтра
    public static FilterUsageDescription getFilterUsageDescription(String filterTableAlias) {
        return getFilterUsageDescription(filterTableAlias, "info_registry");
    }

    // Alias: ОписаниеИспользованиеФильтра
    public static FilterUsageDescription getFilterUsageDescription(String filterTableAlias, String registryTableAlias) {
        return getFilterUsageDescription(filterTableAlias, registryTableAlias, false);
    }

    // Alias: ОписаниеИспользованиеФильтра
    // TODO: Можно это вынести в конструктор класса
    public static FilterUsageDescription getFilterUsageDescription(String filterTableAlias, String registryTableAlias, boolean forceFilterAsTT) {
        FilterUsageDescription filterUsageDescription = new FilterUsageDescription();
        filterUsageDescription.setFilterTableAlias(filterTableAlias);
        filterUsageDescription.setRegistryTableAlias(registryTableAlias);

        if (forceFilterAsTT) {
            filterUsageDescription.setFilterAsTT(true);
        }

        return filterUsageDescription;
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public static void initializeFilterUsage(FilterUsageDescription filterUsageDescription, Object filter, HRBD_RegistryDescription registryDescription,
                                             String periodFields, HRPR_QueryDescriptionOperator queryOperator) {
        initializeFilterUsage(filterUsageDescription, filter, registryDescription, periodFields, queryOperator, "");
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public static void initializeFilterUsage(FilterUsageDescription filterUsageDescription, Object filter, HRBD_RegistryDescription registryDescription,
                                             String periodFields, HRPR_QueryDescriptionOperator queryOperator, String parameterNamePostfix) {
        initializeFilterUsage(filterUsageDescription, filter, registryDescription, periodFields, queryOperator, parameterNamePostfix, false);
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public static void initializeFilterUsage(FilterUsageDescription filterUsageDescription, Object filter, HRBD_RegistryDescription registryDescription,
                                             String periodFields, HRPR_QueryDescriptionOperator queryOperator, String parameterNamePostfix, boolean allRecords) {
        filterUsageDescription.setParameterNamePostfix(parameterNamePostfix);
        filterUsageDescription.setQueryOperator(queryOperator);
        filterUsageDescription.setAllRecords(allRecords);

        if (!filterUsageDescription.isFilterAsTT()) {
            filterUsageDescription.setFilterAsTT(isUseTTFilter(filter, allRecords));
        }

        if (filterUsageDescription.getRegistryTableAlias() != null && !filterUsageDescription.getRegistryTableAlias().isEmpty()) {
            HRPR_QueryDescriptionJoin filterTableJoinDescription = getJoinDescriptionWithFilterTable(queryOperator,
                    filterUsageDescription.getFilterTableAlias(), filterUsageDescription.getRegistryTableAlias());

            if (filterTableJoinDescription == null) {
                throw new IllegalStateException("Cannot put filter in query getting registry data");
            }

            filterTableJoinDescription.setTemplateJoinCondition(HRPR_SQLGeneration.getConditionsString(filterTableJoinDescription.getConditions()));
            filterTableJoinDescription.getConditions().clear();
        }

        for (String dimension : filter.getFilterDimensions()) {
            String filterField = getFilterTableField(filter, dimension);
            filterUsageDescription.getFilterDimensions().put(dimension, filterField);

            if (filter.getDimensionsForSearch().get(dimension.toUpperCase()) != null) {
                filterUsageDescription.getJoinDimensions().put(dimension, filterField);
            }
        }

        List<String> periodFieldsArray = StringUtils.splitStringToSubstringArray(periodFields, ",", true, true);

        for (String periodField : periodFieldsArray) {
            String filterField = getFilterTableField(filter, periodField);
            filterUsageDescription.getFilterPeriodFields().put(periodField, filterField);
        }

        for (String additionalField : filter.getAdditionalFilterFields()) {
            if (filterUsageDescription.isFilterAsTT()) {
                filterUsageDescription.getAdditionalFields().put(additionalField, filterUsageDescription.getFilterTableAlias() + "." + additionalField);
            } else {
                filterUsageDescription.getAdditionalFields().put(additionalField, additionalField + parameterNamePostfix);
            }
        }
    }



    // Alias: ОписаниеСоединенияСТаблицейФильтра
    public static HRPR_QueryDescriptionJoin getJoinDescriptionWithFilterTable(HRPR_QueryDescriptionOperator queryOperator, String filterTableAlias, String registryTableAlias) {
        List<HRPR_QueryDescriptionJoin> joins = queryOperator.getJoins().stream()
                .filter(join -> join.getLeadingTable().equals(filterTableAlias) && join.getJoiningTable().equals(registryTableAlias))
                .toList();

        if (joins.isEmpty()) {
            joins = queryOperator.getJoins().stream()
                    .filter(join -> join.getLeadingTable().equals(registryTableAlias) && join.getJoiningTable().equals(filterTableAlias))
                    .toList();
        }

        if (joins.size() == 1) {
            return joins.get(0);
        }

        return null;
    }

    // Alias: ИспользоватьВТФильтр
    static boolean isUseTTFilter(Object filter, boolean allRecords) {
        if (filter.getFilterValue().getType() == TYPE_FILTER_VALUE_LIST) {
            return allRecords;
        }

        return true;
    }

    // Alias: ПолеТаблицыФильтра
    static String getFilterTableField(Object filter, String registryFilterFieldName) {
        if (filter.getRegistryDimensionFilterDimensionMap().contains(registryFilterFieldName)) {
            return filter.getRegistryDimensionFilterDimensionMap().get(registryFilterFieldName);
        }

        return registryFilterFieldName;
    }

    private static final String TYPE_FILTER_VALUE_LIST = "filter_value_list";
    private static final String TYPE_FILTER_VALUE_MAP = "filter_value_map";
    private static final String TYPE_FILTER_TEMP_TABLE = "filter_temp_table";
}
