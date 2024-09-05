package net.letsdank.platform.module.salary.hr.pr;

// Вспомогательные методы
public class SalaryHRPRUtils {
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
