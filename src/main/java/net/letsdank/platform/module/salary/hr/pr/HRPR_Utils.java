package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;

// Вспомогательные методы
public class HRPR_Utils {
    // Alias: ИмяВспомогательнойВТДоступныеЗаписи
    public static String getAdditionalTTNameAvailableRecords(String registryName, TTRegistryNameBuildContext buildContext) {
        return "vt_available_records_" + registryName;
    }

    // Alias: ИмяВспомогательнойВТПериодыСрезаНаНачалоДня
    public static String getAdditionalTTNamePeriodsOfSliceByStartDay(String registryName, TTRegistryNameBuildContext buildContext) {
        return "vt_periods_of_slice_by_start_day_" + registryName;
    }

    // Alias: УникальноеИмяПараметраЗапроса
    public static String getUniqueQueryParameterName(String tableName) {
        return getUniqueQueryParameterName(tableName, null);
    }

    // Alias: УникальноеИмяПараметраЗапроса
    public static String getUniqueQueryParameterName(String tableName, Integer parameterIndex) {
        String uniqueParameterName = tableName + "_param";

        if (parameterIndex != null) {
            uniqueParameterName += parameterIndex;
            parameterIndex++;
        }

        return uniqueParameterName;
    }

    // Alias: ДоступенИнтервальныйРегистрСведений
    // Alias: ИспользоватьИнтервальныйРегистрСведений (один и тот же функционал)
    public static boolean isAvailableIntervalRegistry(String registryName) {
        // TODO: implement
        return false;
    }
}
