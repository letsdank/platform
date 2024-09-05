package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.utils.platform.sql.SQLQuery;

// Методы получения данных
public class SalaryHRPRGetData {
    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static SQLQuery runQueryToGetRegistryTransactions(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static SQLQuery runQueryToGetRegistryTransactions(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        return runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ВыполнитьЗапросПолученияДвиженийРегистра
    public static SQLQuery runQueryToGetRegistryTransactions(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName;
        }

        // TODO: Implement
        return null;
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static SQLQuery runQueryToGetRegistrySlice(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static SQLQuery runQueryToGetRegistrySlice(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static SQLQuery runQueryToGetRegistrySlice(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, boolean sliceLast) {
        return runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, sliceLast, null);
    }

    // Alias: ВыполнитьЗапросПолученияСрезаРегистра
    public static SQLQuery runQueryToGetRegistrySlice(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, boolean sliceLast, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName + (sliceLast ? "_slice_last" : "_slice_first");
        }

        // TODO: Implement
        return null;
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static SQLQuery runQueryToGetRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, null);
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static SQLQuery runQueryToGetRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        return runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ВыполнитьЗапросПолученияПериодовРегистра
    public static SQLQuery runQueryToGetRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName + "_periods";
        }

        // TODO: Implement
        return null;
    }
}
