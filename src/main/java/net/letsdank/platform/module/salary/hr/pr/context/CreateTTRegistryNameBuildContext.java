package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_FilterDescription;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.utils.platform.metadata.registry.InfoRegistryPeriodicity;

import java.util.List;

@Getter
@Setter
public class CreateTTRegistryNameBuildContext extends TTRegistryNameBuildContext {
    // TODO: JavaDoc
    private boolean includeRecordsAtPeriodStart;
    private String ttNameAtPeriodStart;
    private String usedTTNameAtPeriodStart;
    private List<HRBD_FilterDescription> filtersAtPeriodStart;

    // Alias: ПараметрыПостроенияДляСоздатьВТИмяРегистра
    public CreateTTRegistryNameBuildContext() {
        super();
        includeRecordsAtPeriodStart = false;
        ttNameAtPeriodStart = "";
        usedTTNameAtPeriodStart = "";
        filtersAtPeriodStart = null;
    }

    // Alias: ВключатьЗаписиНаНачалоПериода
    @Override
    public boolean isIncludeRecordsAtPeriodStart(HRBD_RegistryDescription registryDescription) {
        return includeRecordsAtPeriodStart &&
            (
                registryDescription.getPeriodicity() == InfoRegistryPeriodicity.DAY ||
                registryDescription.getPeriodicity() == InfoRegistryPeriodicity.MONTH ||
                registryDescription.getPeriodicity() == InfoRegistryPeriodicity.QUARTER ||
                registryDescription.getPeriodicity() == InfoRegistryPeriodicity.YEAR
            );
    }
}
