package net.letsdank.platform.module.salary.hr.pr.filter;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateTTRegistryNameFilter<T extends HRPR_FilterValue> {
    private Map<String, String> registryDimensionFilterDimensionMap = new HashMap<>();
    private final List<String> filterDimensions;
    private final List<String> additionalFilterFields;
    private T filterValue;

    // Alias: НовыйОписаниеФильтраДляСоздатьВТИмяРегистра
    public CreateTTRegistryNameFilter() {
        this("", "");
    }

    // Alias: НовыйОписаниеФильтраДляСоздатьВТИмяРегистра
    public CreateTTRegistryNameFilter(String dimensions, String additionalFields) {
        filterDimensions = StringUtils.splitStringToSubstringArray(dimensions, ",", null, true);
        additionalFilterFields = StringUtils.splitStringToSubstringArray(additionalFields, ",", null, true);
    }

    // Alias: НовыйОписаниеФильтраДляСоздатьВТИмяРегистра
    public CreateTTRegistryNameFilter(List<String> dimensions, List<String> additionalFields) {
        filterDimensions = dimensions;
        additionalFilterFields = additionalFields;
    }

    // Alias: ПолеТаблицыФильтра
    public String getFilterTableField(String registryFilterFieldName) {
        if (registryDimensionFilterDimensionMap.containsKey(registryFilterFieldName)) {
            return registryDimensionFilterDimensionMap.get(registryFilterFieldName);
        }

        return registryFilterFieldName;
    }

    // Alias: ВсеПоляТаблицыФильтра
    public List<String> getFilterTableAllFields() {
        return getFilterTableAllFields(null);
    }

    // Alias: ВсеПоляТаблицыФильтра
    public List<String> getFilterTableAllFields(Map<String, String> periodFields) {
        List<String> fields = new ArrayList<>();

        for (String dimension : filterDimensions) {
            String filterField = getFilterTableField(dimension);
            fields.add(filterField);
        }

        for (String field : additionalFilterFields) {
            String filterField = getFilterTableField(field);
            fields.add(filterField);
        }

        if (periodFields != null) {
            fields.addAll(periodFields.values());
        }

        return fields;
    }

    // Alias: ИспользоватьВТФильтр
    public boolean isUseTTFilter(boolean allRecords) {
        if (filterValue.getType().equals(HRPR_FilterUsageDescription.TYPE_FILTER_VALUE_LIST)) {
            return allRecords;
        }

        return true;
    }
}
