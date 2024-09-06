package net.letsdank.platform.module.salary.hr.pr;

// Вспомогательные методы
public class HRPR_Utils {
    // TODO: Определиться с ООП
    // Alias: ИмяВспомогательнойВТДоступныеЗаписи
    public static String getAdditionalTTNameAvailableRecords(String registryName, Object buildContext) {
        return "vt_" + registryName;
    }

    // Alias: ДоступенИнтервальныйРегистрСведений
    public static boolean isAvailableIntervalRegistry(String registryName) {
        // TODO: implement
        return false;
    }
}
