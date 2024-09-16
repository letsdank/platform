package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.HRPR_SQLGeneration;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionJoin;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionOperator;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodFieldDescription;
import net.letsdank.platform.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRPR_FilterUsageDescription {
    public static final String TYPE_FILTER_VALUE_LIST = "filter_value_list";
    public static final String TYPE_FILTER_VALUE_MAP = "filter_value_map";
    public static final String TYPE_FILTER_TEMP_TABLE = "filter_temp_table";

    private String filterTableAlias;
    private String registryTableAlias;
    private String parameterNamePostfix;
    private String templateJoinCondition = "";
    private List<String> joinConditions = new ArrayList<>();
    private Map<String, String> filterDimensions; // TODO правильно?
    private Map<String, String> filterPeriodFields; // TODO
    private Map<String, String> additionalFields; // TODO
    private Map<String, String> joinDimensions; // TODO правильно?
    private HRPR_QueryDescriptionOperator queryOperator;
    private boolean filterAsTT = false;
    private boolean allRecords = false;
    private List<HRPR_PeriodFieldDescription> calculatedPeriodParametersDescription = new ArrayList<>();

    // Alias: ОписаниеИспользованиеФильтра
    public HRPR_FilterUsageDescription() {
        this("date_dimensions");
    }

    // Alias: ОписаниеИспользованиеФильтра
    public HRPR_FilterUsageDescription(String filterTableAlias) {
        this(filterTableAlias, "info_registry");
    }

    // Alias: ОписаниеИспользованиеФильтра
    public HRPR_FilterUsageDescription(String filterTableAlias, String registryTableAlias) {
        this(filterTableAlias, registryTableAlias, false);
    }

    // Alias: ОписаниеИспользованиеФильтра
    public HRPR_FilterUsageDescription(String filterTableAlias, String registryTableAlias, boolean forceFilterAsTT) {
        this.filterTableAlias = filterTableAlias;
        this.registryTableAlias = registryTableAlias;

        if (forceFilterAsTT) {
            filterAsTT = true;
        }
    }

    // Alias: ВыражениеИзмерениеФильтра
    public String getFilterDimensionExpression(String dimension) {
        if (filterAsTT) {
            String filterFieldName = filterDimensions.get(dimension);
            return filterTableAlias + "." + filterFieldName;
        }

        return "&" + HRPR_FilterValue.getParameterNameByFilterElement(dimension, parameterNamePostfix);
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public void initialize(CreateTTRegistryNameFilter<?> filter, HRBD_RegistryDescription registryDescription,
                           String periodFields, HRPR_QueryDescriptionOperator operator) {
        initialize(filter, registryDescription, periodFields, operator, "");
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public void initialize(CreateTTRegistryNameFilter<?> filter, HRBD_RegistryDescription registryDescription,
                           String periodFields, HRPR_QueryDescriptionOperator operator,
                           String parameterNamePostfix) {
        initialize(filter, registryDescription, periodFields, operator, parameterNamePostfix, false);
    }

    // Alias: ИнициализироватьИспользованиеФильтра
    public void initialize(CreateTTRegistryNameFilter<?> filter, HRBD_RegistryDescription registryDescription,
                           String periodFields, HRPR_QueryDescriptionOperator operator,
                           String parameterNamePostfix, boolean allRecords) {
        this.parameterNamePostfix = parameterNamePostfix;
        this.queryOperator = operator;
        this.allRecords = allRecords;

        if (!filterAsTT) {
            filterAsTT = isUseTTFilter(filter, allRecords);
        }

        if (registryTableAlias != null && !registryTableAlias.isEmpty()) {
            HRPR_QueryDescriptionJoin filterTableJoinDescription = getJoinDescriptionWithTableFilter();

            if (filterTableJoinDescription == null) {
                throw new IllegalStateException("Cannot put filter in query getting registry data");
            }

            templateJoinCondition = HRPR_SQLGeneration.getConditionsString(filterTableJoinDescription.getConditions());
            filterTableJoinDescription.getConditions().clear();
        }

        for (String dimension : filter.getFilterDimensions()) {
            String filterField = filter.getFilterTableField(dimension);
            filterDimensions.put(dimension, filterField);

            if (registryDescription.getDimensionsForSearch().get(dimension.toUpperCase()) != null) {
                joinDimensions.put(dimension, filterField);
            }
        }

        List<String> periodFieldsList = StringUtils.splitStringToSubstringArray(periodFields, ",", true, true);

        for (String periodField : periodFieldsList) {
            String filterField = filter.getFilterTableField(periodField);
            filterPeriodFields.put(periodField, filterField);
        }

        for (String additionalField : filter.getAdditionalFilterFields()) {
            if (filterAsTT) {
                additionalFields.put(additionalField, filterTableAlias + "." + additionalField);
            } else {
                additionalFields.put(additionalField, additionalField + parameterNamePostfix);
            }
        }
    }

    // Alias: УстановитьВыражениеПериодаВТекстШаблонаУсловияСвязи
    public void putPeriodExpressionInJoinTemplate(String replaceableText, HRPR_PeriodFieldDescription periodExpressionDescription) {
        String periodFieldExpression = periodExpressionDescription.getPeriodFieldExpression();
        templateJoinCondition = templateJoinCondition.replace(replaceableText, periodFieldExpression);
    }

    // Alias: ОписаниеСоединенияСТаблицейФильтра
    public HRPR_QueryDescriptionJoin getJoinDescriptionWithTableFilter() {
        return queryOperator.getJoinDescriptionWithTableFilter(filterTableAlias, registryTableAlias);
    }

    //
    // PeriodFieldDescription
    //

    // Alias: ДобавитьОписаниеПоляПериодФильтра
    public HRPR_PeriodFieldDescription addPeriodOfFilter(String sourceFieldName, String calculatedFieldAlias) {
        String fieldNameFilterTable = filterPeriodFields.get(sourceFieldName);
        HRPR_PeriodFieldDescription fieldDescription;

        if (filterAsTT) {
            fieldDescription = new HRPR_PeriodFieldDescription(fieldNameFilterTable, filterTableAlias);
        } else {
            String parameterName = HRPR_FilterValue.getParameterNameByFilterElement(calculatedFieldAlias, parameterNamePostfix);

            fieldDescription = new HRPR_PeriodFieldDescription(parameterName);
            fieldDescription.setSourceFieldName(fieldNameFilterTable);
        }

        calculatedPeriodParametersDescription.add(fieldDescription);
        return fieldDescription;
    }
}
