package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.entity.RegistryQueriesDescriptionPacket;

// Формирование запросов к регистрам
public class HRPR_RegistryQuery {
    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter, TTRegistryNameBuildContext buildContext) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter,
                                       TTRegistryNameBuildContext buildContext, String resultTTName) {
        if (buildContext instanceof CreateTTRegistryNameBuildContext) {
            ((CreateTTRegistryNameBuildContext) buildContext).setTtNameAtPeriodStart("");
        }

        String ttNameAvailableRecords = HRPR_Utils.getAdditionalTTNameAvailableRecords(registryName, buildContext);
        if (HRPR_Utils.isAvailableIntervalRegistry(registryName)) {
            if (HRPR_QueryBuildOptions.useFirstRegistry(buildContext, registryName)) {

            }
        }

        // TODO: implement
    }
}
