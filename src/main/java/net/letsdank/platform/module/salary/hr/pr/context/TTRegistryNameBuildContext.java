package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_Filter;
import net.letsdank.platform.utils.platform.metadata.registry.InfoRegistryPeriodicity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TTRegistryNameBuildContext {
    // TODO: JavaDoc
    private List<String> indexingBy;
    private List<HRPR_Filter> filters;
    private boolean formPeriodicityDay;
    private boolean useLanguageQueryExtensionForComposition;
    private Map<String, Object> constantFields;
    private boolean excludedRegisters;
    private Map<String, String> fieldAliasCorrespondence;
    private Map<String, String> fieldAliasCorrespondenceForComposition;
    private boolean excludeUnusedFields;

    // Alias: ПараметрыПостроенияВТИмяРегистра
    public TTRegistryNameBuildContext() {
        filters = new ArrayList<>();
        formPeriodicityDay = true;
        useLanguageQueryExtensionForComposition = true;
        excludedRegisters = false;
        fieldAliasCorrespondence = new HashMap<>();
        fieldAliasCorrespondenceForComposition = new HashMap<>();
        excludeUnusedFields = true;
    }

    // Alias: ФормироватьСПериодичностьДень
    public boolean isFormFromPeriodicityDay(HRBD_RegistryDescription registryDescription) {
        return formPeriodicityDay && registryDescription.getPeriodicity() == InfoRegistryPeriodicity.SECOND;
    }

    // Alias: ВключатьЗаписиНаНачалоПериода
    public boolean isIncludeRecordsAtPeriodStart(HRBD_RegistryDescription registryDescription) {
        // Для TTRegistryNameBuildContext важно наличие includeRecordsAtPeriodStart, поэтому возвращаем false
        return false;
    }

    // Alias: ИспользоватьПервичныйРегистр
    public boolean isUseFirstRegistry(String registryName) {
        return isUseFirstRegistry(registryName, false);
    }

    // Alias: ИспользоватьПервичныйРегистр
    public boolean isUseFirstRegistry(String registryName, boolean isSlice) {
        return isUseFirstRegistry(registryName, isSlice, 1);
    }

    // Alias: ИспользоватьПервичныйРегистр
    // TODO: countAllowedExcludedRegistrators - правильно переведено?
    public boolean isUseFirstRegistry(String registryName, boolean isSlice, int countAllowedExcludedRegistrators) {
        if (filters == null) {
            return false;
        }

        // TODO: implement
        for (HRPR_Filter filter : filters) {

        }

        return true;
    }
}
