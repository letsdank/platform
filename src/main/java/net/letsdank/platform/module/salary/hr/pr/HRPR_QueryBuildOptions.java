package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;

// Параметры построения запросов
public class HRPR_QueryBuildOptions {
    // Alias: ИспользоватьПервичныйРегистр
    static boolean useFirstRegistry(TTRegistryNameBuildContext buildContext, String registryName) {
        return useFirstRegistry(buildContext, registryName, false);
    }

    // Alias: ИспользоватьПервичныйРегистр
    static boolean useFirstRegistry(TTRegistryNameBuildContext buildContext, String registryName, boolean isSlice) {
        return useFirstRegistry(buildContext, registryName, isSlice, 1);
    }

    // Alias: ИспользоватьПервичныйРегистр
    // TODO: countAllowedExcludedRegistrator - правильно переведено?
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
