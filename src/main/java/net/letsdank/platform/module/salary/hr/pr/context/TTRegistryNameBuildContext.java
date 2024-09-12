package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TTRegistryNameBuildContext {
    // TODO: JavaDoc
    private Object indexingBy;
    private List<HRPR_Filter> filters;
    private boolean formPeriodicityDay;
    private boolean useLanguageQueryExtensionForComposition;
    private List<Object> constantFields;
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
}
