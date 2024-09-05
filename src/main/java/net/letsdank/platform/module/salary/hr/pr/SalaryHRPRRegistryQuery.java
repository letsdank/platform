package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.entity.RegistryQueriesDescriptionPacket;

// Формирование запросов к регистрам
public class SalaryHRPRRegistryQuery {
    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, null);
    }
    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter, TTRegistryNameBuildContext buildContext) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, buildContext, null);
    }

    static void addQueryTTRegistryName(RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter,
                                       TTRegistryNameBuildContext buildContext, String resultTTName) {
        if (buildContext instanceof CreateTTRegistryNameBuildContext) {
            ((CreateTTRegistryNameBuildContext) buildContext).setTtNameAtPeriodStart("");
        }

        String ttNameAvailableRecords = SalaryHRPRUtils.getAdditionalTTNameAvailableRecords(registryName, buildContext);
        if (SalaryHRPRUtils.isAvailableIntervalRegistry(registryName)) {

        }

        // TODO: implement
    }
}
