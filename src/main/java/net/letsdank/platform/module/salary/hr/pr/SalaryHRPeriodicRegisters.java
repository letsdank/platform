package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.entity.RegistryQueriesDescriptionPacket;
import net.letsdank.platform.module.salary.hr.pr.event.SalaryHRPROnTTRegistryNameQueryEvent;
import net.letsdank.platform.utils.event.EventPublisherHolder;
import net.letsdank.platform.utils.platform.sql.SQLQuery;

import java.util.Map;

// Alias: ЗарплатаКадрыПериодическиеРегистры
public class SalaryHRPeriodicRegisters {

    /**
     * Returns registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистра
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryName(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return getTTRegistryName(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистра
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryName(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        SQLQuery queryResult = HRPR_GetData.runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, "");
        return queryResult.execute();
    }

    /**
     * Returns slice last registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистраСрезПоследних
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryNameSliceLast(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return getTTRegistryNameSliceLast(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns slice last registry map.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистраСрезПоследних
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryNameSliceLast(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        SQLQuery queryResult = HRPR_GetData.runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true, "");
        return queryResult.execute();
    }

    /**
     * Creates temporary table of transactions in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистра
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistry(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistry(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistry(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistryTransactions(registryName, ttManager, onlyDistrict, filter, buildContext, resultTTName);
    }

    /**
     * Returns table of registry periods.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистраПериоды
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
        return getTTRegistryPeriods(registryName, ttManager, onlyDistrict, filter, null);
    }

    /**
     * Returns table of registry periods.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     * @param buildContext TODO
     * @return Map
     */
    // Alias: ТаблицаВТИмяРегистраПериоды
    // TODO: ttManager - Should be TempTableManager
    public static Map<String, Object> getTTRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
        SQLQuery queryResult = HRPR_GetData.runQueryToGetRegistryPeriods(registryName, ttManager, onlyDistrict, filter, buildContext, "");
        return queryResult.execute();
    }

    /**
     * Creates temporary table of periods in registry to <code>ttManager</code>.
     * @param registryName Periodic registry name, how it sets into the registry.
     * @param ttManager TempTableManager
     * @param onlyDistrict Allow only district.
     * @param filter TODO
     */
    // Alias: СоздатьВТИмяРегистраПериоды
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistryPeriods(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceFirst(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceFirst(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceFirst(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceLast(String registryName, Object ttManager, boolean onlyDistrict, Object filter) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceLast(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext) {
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
    // TODO: ttManager - Should be TempTableManager
    public static void createTTRegistrySliceLast(String registryName, Object ttManager, boolean onlyDistrict, Object filter, Object buildContext, String resultTTName) {
        HRPR_GetData.runQueryToGetRegistrySlice(registryName, ttManager, onlyDistrict, filter, buildContext, true, resultTTName);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, Object filter) {
        return getQueryTTRegistryName(registryName, onlyDistrict, filter, null);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, Object filter, TTRegistryNameBuildContext buildContext) {
        return getQueryTTRegistryName(registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ЗапросВТИмяРегистра
    public static SQLQuery getQueryTTRegistryName(String registryName, boolean onlyDistrict, Object filter, TTRegistryNameBuildContext buildContext, String resultTTName) {
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

        RegistryQueriesDescriptionPacket packet = new RegistryQueriesDescriptionPacket();
        HRPR_RegistryQuery.addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, buildContext, resultTTName);

        return HRPR_SQLQuery.getQueryByDescriptionPacket(packet, buildContext.isUseLanguageQueryExtensionForReports());
    }
}
