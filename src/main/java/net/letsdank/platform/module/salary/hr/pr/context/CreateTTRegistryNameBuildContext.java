package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTTRegistryNameBuildContext extends TTRegistryNameBuildContext {
    // TODO: JavaDoc
    private boolean includeRecordsAtPeriodStart;
    private String ttNameAtPeriodStart;
    private String usedTTNameAtPeriodStart;
    private List<Object> filtersAtPeriodStart;

    // Alias: ПараметрыПостроенияДляСоздатьВТИмяРегистра
    public CreateTTRegistryNameBuildContext() {
        super();
        includeRecordsAtPeriodStart = false;
        ttNameAtPeriodStart = "";
        usedTTNameAtPeriodStart = "";
        filtersAtPeriodStart = null;
    }
}
