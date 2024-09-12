package net.letsdank.platform.module.salary.hr.pr.period;

import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.HRPR_QueryBuildOptions;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.utils.platform.metadata.registry.InfoRegistryPeriodicity;

public enum HRPR_PeriodMultiplicity {
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    QUARTER,
    YEAR;

    public static HRPR_PeriodMultiplicity getMultiplicity(HRBD_RegistryDescription registryDescription, TTRegistryNameBuildContext buildContext) {
        boolean formFromPeriodicityDay = buildContext.isFormFromPeriodicityDay(registryDescription);

        return switch (registryDescription.getPeriodicity()) {
            case SECOND -> formFromPeriodicityDay ? DAY : SECOND;
            case DAY -> DAY;
            case MONTH -> MONTH;
            case QUARTER -> QUARTER;
            case YEAR -> YEAR;
        };
    }
}
