package net.letsdank.platform.module.salary.hr.pr;

// Вспомогательные методы
public class HRPR_Utils {
    // TODO: Определиться с ООП
    // Alias: ИмяВспомогательнойВТДоступныеЗаписи
    public static String getAdditionalTTNameAvailableRecords(String registryName, Object buildContext) {
        return "vt_" + registryName;
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
    public static boolean isAvailableIntervalRegistry(String registryName) {
        // TODO: implement
        return false;
    }
}
