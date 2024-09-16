package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_RegistryQueriesDescriptionPacket;
import net.letsdank.platform.module.salary.hr.pr.event.SalaryHRPROnTTRegistryNameQueryEvent;
import net.letsdank.platform.module.salary.hr.pr.filter.CreateTTRegistryNameFilter;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterValueList;
import net.letsdank.platform.utils.data.TableMap;
import net.letsdank.platform.utils.event.EventPublisherHolder;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import net.letsdank.platform.utils.platform.sql.TempTableManager;

import java.util.List;
import java.util.Map;

// Alias: ЗарплатаКадрыПериодическиеРегистры
public class SalaryHRPeriodicRegisters {

    /**
     * Returns registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter CreateTTRegistryNameFilter with HRPR_FilterValueList value
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистра
    public static List<Map<String, Object>> getTTRegistryName(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                             CreateTTRegistryNameFilter<HRPR_FilterValueList> filter) {
        return getTTRegistryName(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter CreateTTRegistryNameFilter with HRPR_FilterValueList value
     * @param buildContext CreateTTRegistryNameBuildContext
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистра
    public static List<Map<String, Object>> getTTRegistryName(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                                              CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                              CreateTTRegistryNameBuildContext buildContext) {
        TableMap queryResult = HRPR_GetData.runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, "");
        return queryResult.toTableMap();
    }

    /**
     * Returns slice last registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистраСрезПоследних
    public static List<Map<String, Object>> getTTRegistryNameSliceLast(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        return getTTRegistryNameSliceLast(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns slice last registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистраСрезПоследних
    public static List<Map<String, Object>> getTTRegistryNameSliceLast(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        TableMap queryResult = HRPR_GetData.runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true, "");
        return queryResult.toTableMap();
    }

    /**
     * Creates temporary table of transactions in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистра
    public static void createTTRegistry(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                        CreateTTRegistryNameFilter<HRPR_FilterValueList> filter) {
        createTTRegistry(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Creates temporary table of transactions in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     */
    // Alias: СоздатьВТИмяРегистра
    public static void createTTRegistry(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                        CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                        CreateTTRegistryNameBuildContext buildContext) {
        createTTRegistry(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    /**
     * Creates temporary table of transactions in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @param resultTTName Name of resulting temporary table, if not set, it will be generated as <code>vt_&lt;registryName&gt;</code>.
     */
    // Alias: СоздатьВТИмяРегистра
    public static void createTTRegistry(String registryName, TempTableManager ttManager, boolean onlyDistrict,
                                        CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                        CreateTTRegistryNameBuildContext buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, resultTTName);
    }

    /**
     * Returns table of registry periods.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистраПериоды
    public static List<Map<String, Object>> getTTRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        return getTTRegistryPeriods(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns table of registry periods.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @return table map
     */
    // Alias: ТаблицаВТИмяРегистраПериоды
    public static List<Map<String, Object>> getTTRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        TableMap queryResult = HRPR_GetData.runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, "");
        return queryResult.toTableMap();
    }

    /**
     * Creates temporary table of periods in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистраПериоды
    public static void createTTRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        createTTRegistryPeriods(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Creates temporary table of periods in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     */
    // Alias: СоздатьВТИмяРегистраПериоды
    public static void createTTRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        createTTRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    /**
     * Creates temporary table of periods in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @param resultTTName Name of resulting temporary table, if not set, it will be generated as <code>vt_&lt;registryName&gt;_periods</code>.
     */
    // Alias: СоздатьВТИмяРегистраПериоды
    public static void createTTRegistryPeriods(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, resultTTName);
    }

    /**
     * Creates temporary table of slice first in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистраСрезПервых
    public static void createTTRegistrySliceFirst(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        createTTRegistrySliceFirst(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Creates temporary table of slice first in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     */
    // Alias: СоздатьВТИмяРегистраСрезПервых
    public static void createTTRegistrySliceFirst(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        createTTRegistrySliceFirst(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    /**
     * Creates temporary table of slice first in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @param resultTTName Name of resulting temporary table, if not set, it will be generated as <code>vt_&lt;registryName&gt;_slice_first</code>.
     */
    // Alias: СоздатьВТИмяРегистраСрезПервых
    public static void createTTRegistrySliceFirst(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, false, resultTTName);
    }

    /**
     * Creates temporary table of slice last in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистраСрезПоследних
    public static void createTTRegistrySliceLast(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter) {
        createTTRegistrySliceLast(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Creates temporary table of slice last in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     */
    // Alias: СоздатьВТИмяРегистраСрезПоследних
    public static void createTTRegistrySliceLast(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        createTTRegistrySliceLast(registryName, ttManager, onlyDistrict, filter, buildContext, null);
    }

    /**
     * Creates temporary table of slice last in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @param resultTTName Name of resulting temporary table, if not set, it will be generated as <code>vt_&lt;registryName&gt;_slice_last</code>.
     */
    // Alias: СоздатьВТИмяРегистраСрезПоследних
    public static void createTTRegistrySliceLast(String registryName, TempTableManager ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true, resultTTName);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter) {
        return getQueryTTRegistryName(registryName, onlyDistrict, filter, null);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                  TTRegistryNameBuildContext buildContext) {
        return getQueryTTRegistryName(registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                  TTRegistryNameBuildContext buildContext, String resultTTName) {
        if (resultTTName == null) {
            resultTTName = "vt_" + registryName;
        }

        if (buildContext == null) {
            buildContext = new CreateTTRegistryNameBuildContext();
        }

        SQLQuery query = null;
        EventPublisherHolder.publishEvent(new SalaryHRPROnTTRegistryNameQueryEvent(new SalaryHRPeriodicRegisters(), query,
                registryName, onlyDistrict,
                filter, buildContext, resultTTName));

        if (query != null) {
            return query;
        }

        HRPR_RegistryQueriesDescriptionPacket packet = new HRPR_RegistryQueriesDescriptionPacket();
        HRPR_RegistryQuery.addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, buildContext, resultTTName);

        return HRPR_SQLQuery.getQueryByDescriptionPacket(packet, buildContext.isUseLanguageQueryExtensionForComposition());
    }
}
