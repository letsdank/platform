package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TTRegistryNameBuildContext {
    private Object indexingBy;
    private List<Object> filters;
    private boolean formPeriodicityDay;
    private boolean useLanguageQueryExtensionForReports;
    private List<Object> constantFields;
    private boolean excludedRegisters;
    private Map<String, String> fieldAliasCorrespondence;
    private Map<String, String> fieldAliasCorrespondenceForReports;
    private boolean excludeUnusedFields;

    public TTRegistryNameBuildContext() {
        filters = new ArrayList<>();
        formPeriodicityDay = true;
        useLanguageQueryExtensionForReports = true;
        excludedRegisters = false;
        fieldAliasCorrespondence = new HashMap<>();
        fieldAliasCorrespondenceForReports = new HashMap<>();
        excludeUnusedFields = true;
    }
}
