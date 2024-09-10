package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.utils.platform.metadata.registry.InfoRegistryPeriodicity;

// Параметры построения запросов
public class HRPR_QueryBuildOptions {
    // Alias: ФормироватьСПериодичностьДень
    public static boolean isFormFromPeriodicityDay(TTRegistryNameBuildContext buildContext, HRBD_RegistryDescription registryDescription) {
        return buildContext.isFormPeriodicityDay() && registryDescription.getPeriodicity() == InfoRegistryPeriodicity.SECOND;
    }

    // Alias: ВключатьЗаписиНаНачалоПериода
    public static boolean isIncludeRecordsAtPeriodStart(TTRegistryNameBuildContext buildContext, HRBD_RegistryDescription registryDescription) {
        // TODO: Мы можем использовать CreateTTRegistryNameBuildContext или оставить TTRegistryNameBuildContext?
        return buildContext instanceof CreateTTRegistryNameBuildContext && ((CreateTTRegistryNameBuildContext)buildContext).isIncludeRecordsAtPeriodStart() &&
                (
                        registryDescription.getPeriodicity() == InfoRegistryPeriodicity.DAY ||
                        registryDescription.getPeriodicity() == InfoRegistryPeriodicity.MONTH ||
                        registryDescription.getPeriodicity() == InfoRegistryPeriodicity.QUARTER ||
                        registryDescription.getPeriodicity() == InfoRegistryPeriodicity.YEAR
                );
    }

    // Alias: ИспользоватьПервичныйРегистр
    static boolean useFirstRegistry(TTRegistryNameBuildContext buildContext, String registryName) {
        return useFirstRegistry(buildContext, registryName, false);
    }

    // Alias: ИспользоватьПервичныйРегистр
    static boolean useFirstRegistry(TTRegistryNameBuildContext buildContext, String registryName, boolean isSlice) {
        return useFirstRegistry(buildContext, registryName, isSlice, 1);
    }

    // Alias: ИспользоватьПервичныйРегистр
    // TODO: countAllowedExcludedRegistrators - правильно переведено?
    static boolean useFirstRegistry(TTRegistryNameBuildContext buildContext, String registryName, boolean isSlice, int countAllowedExcludedRegistrators) {
        if (buildContext == null || buildContext.getFilters() == null) {
            return false;
        }

        // TODO: implement
        for (Object filter : buildContext.getFilters()) {

        }

        return true;
    }
}
