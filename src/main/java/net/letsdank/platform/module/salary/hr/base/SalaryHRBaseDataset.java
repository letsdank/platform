package net.letsdank.platform.module.salary.hr.base;

import net.letsdank.platform.module.salary.hr.base.entity.HRBD_FilterDescription;

import java.util.ArrayList;
import java.util.List;

// Alias: ЗарплатаКадрыОбщиеНаборыДанных
public class SalaryHRBaseDataset {
    // Alias: ДобавитьВКоллекциюОтбор
    public static void addFilterIntoCollection(List<HRBD_FilterDescription> collection, String leftName, String compareType, Object rightValue) {
        addFilterIntoCollection(collection, leftName, compareType, rightValue, true);
    }

    // Alias: ДобавитьВКоллекциюОтбор
    public static void addFilterIntoCollection(List<HRBD_FilterDescription> collection, String leftName, String compareType, Object rightValue, boolean relativePath) {
        if (collection == null) {
            collection = new ArrayList<>();
        }

        collection.add(new HRBD_FilterDescription(leftName, compareType, rightValue, relativePath));
    }
}
