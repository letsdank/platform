package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.period.CreateTTRegistryNamePeriodDescription;
import net.letsdank.platform.utils.data.TableMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRPR_FilterValueList extends HRPR_FilterValue {
    private List<String> dimensionValues;
    private Map<String, Object> periodDescription;
    private Map<String, Object> additionalFields;

    // Alias: ФильтрСписокЗначений
    public HRPR_FilterValueList(Map<String, Object> periodDescription) {
        this(periodDescription, (List<String>) null, null);
    }

    // Alias: ФильтрСписокЗначений
    public HRPR_FilterValueList(Map<String, Object> periodDescription, List<String> dimensionValues, Map<String, Object> additionalFields) {
        super(HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_LIST);

        this.periodDescription = periodDescription;
        if (dimensionValues != null) {
            this.dimensionValues = dimensionValues;
        } else {
            this.dimensionValues.add(null);
        }

        if (additionalFields != null) {
            this.additionalFields = additionalFields;
        }
    }

    // Alias: ФильтрСписокЗначений
    public HRPR_FilterValueList(Map<String, Object> periodDescription, String dimensionValue, Map<String, Object> additionalFields) {
        super(HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_LIST);

        this.periodDescription = periodDescription;
        this.dimensionValues.add(dimensionValue);

        if (additionalFields != null) {
            this.additionalFields = additionalFields;
        }
    }

    // Alias: ФильтрСписокЗначенийПоТаблицеЗначений
    public static HRPR_FilterValueList byTableMap(TableMap tableMap, CreateTTRegistryNameFilter<HRPR_FilterValueTableMap> filter) {
        if (filter.getFilterDimensions().size() > 1) {
            return null;
        } else if (tableMap.size() > 50) {
            return null;
        }

        Map<String, Object> periodDescription = CreateTTRegistryNamePeriodDescription.create();

        List<String> groupFields = new ArrayList<>();
        for (TableMap.Column column : tableMap.getColumns()) {
            if (filter.getAdditionalFilterFields().contains(column.name())) {
                groupFields.add(column.name());
            } else if (column.type() == LocalDateTime.class) {

                //
                // TODO: Лонг стори
                //
                // Так как здесь применяется проверка динамических полей, пока оставим как Map, но если в Map
                // больше не будет обращений к объекту как к Map, то можно спокойно проверять наличие
                // полей через статически прописанные в ООП классе переменные, сейчас пока пусть будет так
                //

                if (periodDescription.containsKey(column.name())) {
                    groupFields.add(column.name());
                } else {
                    return null;
                }
            }
        }

        String groupFieldsString = String.join(",", groupFields);
        TableMap tableForUniqueCheck = tableMap.copy(null, groupFieldsString);

        if (tableForUniqueCheck.size() != 1) {
            return null;
        }

        for (String periodFieldKey : periodDescription.keySet()) {
            if (groupFields.contains(periodFieldKey)) {
                // TODO: Точно так должно быть? Вроде наоборот нужно
                periodDescription.put(periodFieldKey, tableForUniqueCheck.getRow(periodFieldKey).getCell(0));
            }
        }

        List<String> dimensionValueList = new ArrayList<>();
        for (String dimension : filter.getFilterDimensions()) {
            dimensionValueList = tableMap.getValues(dimension).stream().map(Object::toString).toList();
            break;
        }

        Map<String, Object> additionalFields = new HashMap<>();
        if (!filter.getAdditionalFilterFields().isEmpty()) {
            for (String field : filter.getAdditionalFilterFields()) {
                additionalFields.put(field, tableForUniqueCheck.getRow(field).getCell(0));
            }
        }

        return new HRPR_FilterValueList(periodDescription, dimensionValueList, additionalFields);
    }
}
