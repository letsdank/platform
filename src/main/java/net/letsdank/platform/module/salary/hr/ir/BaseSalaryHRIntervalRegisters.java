package net.letsdank.platform.module.salary.hr.ir;

import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.entity.HRPR_RegistryQueriesDescriptionPacket;

// Alias: ИнтервальныеРегистрыБЗК
// БЗК - Базовая Зарплата Кадры
public class BaseSalaryHRIntervalRegisters {

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, Object filter) {
        addQueryTTTransactionByIntervalRegistryName(queriesDescriptionPacket, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, Object filter,
                                                                   TTRegistryNameBuildContext buildContext) {
        addQueryTTTransactionByIntervalRegistryName(queriesDescriptionPacket, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, Object filter,
                                                                   TTRegistryNameBuildContext buildContext, String resultTTName) {
        // TODO: implement
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter) {
        addQueryTTTransactionByFirstRegistry(queriesDescriptionPacket, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter,
                                                            TTRegistryNameBuildContext buildContext) {
        addQueryTTTransactionByFirstRegistry(queriesDescriptionPacket, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter,
                                                            TTRegistryNameBuildContext buildContext, String resultTTName) {
        // TODO: Implement
    }
}
