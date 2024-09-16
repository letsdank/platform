package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.event.SalaryHRPROnTTRegistryNameQueryEvent;
import net.letsdank.platform.module.salary.hr.pr.filter.CreateTTRegistryNameFilter;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterValueList;
import net.letsdank.platform.utils.data.TableMap;
import net.letsdank.platform.utils.event.EventPublisherHolder;
import net.letsdank.platform.utils.platform.PUtils;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import net.letsdank.platform.utils.platform.sql.TempTableManager;

// Методы получения данных
public class HRPR_GetData {
    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static TableMap runQueryToGetRegistryTransactions(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                             CreateTTRegistryNameFilter<HRPR_FilterValueList> filter) {
        return runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static TableMap runQueryToGetRegistryTransactions(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                             CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                             CreateTTRegistryNameBuildContext buildContext) {
        return runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static TableMap runQueryToGetRegistryTransactions(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                             CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                             CreateTTRegistryNameBuildContext buildContext, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName;
        }

        if (buildContext == null) {
            buildContext = new CreateTTRegistryNameBuildContext();
        }

        buildContext.setUseLanguageQueryExtensionForComposition(false);

        SQLQuery query = null;
        EventPublisherHolder.publishEvent(new SalaryHRPROnTTRegistryNameQueryEvent(new HRPR_GetData(), query, registryName,
                onlyDistrict, filter, buildContext, resultTTName));

        if (query != null) {
            query.setTempTableManager(ttManager);
            return query.execute();
        } else if (HRPR_Utils.isAvailableIntervalRegistry(registryName) || PUtils.isPrivileged() || buildContext.isIncludeRecordsAtPeriodStart()) {
            query = SalaryHRPeriodicRegisters.getQueryTTRegistryName(registryName, onlyDistrict, filter, buildContext, resultTTName);
            query.setTempTableManager(ttManager);

            return query.execute();
        }

        String ttNameAvailableRecords = HRPR_Utils.getAdditionalTTNameAvailableRecords(registryName, buildContext);
        query = HRPR_RegistryQuery.getQueryTTRegistryNameAvailableRecords(registryName, onlyDistrict, filter, ttNameAvailableRecords, buildContext);
        query.setTempTableManager(ttManager);

        query.execute();

        query = HRPR_RegistryQuery.getQueryTTRegistryTable(registryName, false, filter, ttNameAvailableRecords, buildContext, resultTTName);
        query.setTempTableManager(ttManager);

        PUtils.setPrivileged(true);
        TableMap result = query.execute();
        PUtils.setPrivileged(false);

        return result;
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static TableMap runQueryToGetRegistrySlice(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static TableMap runQueryToGetRegistrySlice(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static TableMap runQueryToGetRegistrySlice(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext, boolean sliceLast) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, sliceLast, null);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static TableMap runQueryToGetRegistrySlice(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                      Object filter, Object buildContext, boolean sliceLast, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName + (sliceLast ? "_slice_last" : "_slice_first");
        }

        // TODO: Implement
        return null;
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static TableMap runQueryToGetRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        return runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static TableMap runQueryToGetRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        return runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static TableMap runQueryToGetRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                        Object filter, Object buildContext, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName + "_periods";
        }

        // TODO: Implement
        return null;
    }
}
