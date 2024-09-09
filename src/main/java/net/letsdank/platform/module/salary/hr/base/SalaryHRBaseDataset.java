package net.letsdank.platform.module.salary.hr.base;

import net.letsdank.platform.module.salary.hr.base.entity.HRBD_FilterDescription;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.string.StringUtils;

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

    // Alias: ОписаниеРегистраСведений
    public static HRBD_RegistryDescription getInfoRegistryDescription(String registryName, Either<String, List<String>> filterDimensions) {
        return getInfoRegistryDescription(registryName, filterDimensions, true);
    }

    // Alias: ОписаниеРегистраСведений
    public static HRBD_RegistryDescription getInfoRegistryDescription(String registryName, Either<String, List<String>> filterDimensions,
                                                                      boolean excludeUnused) {
        List<String> filterDimensionsArray;
        if (filterDimensions.isLeft()) {
            filterDimensionsArray = StringUtils.splitStringToSubstringArray(filterDimensions.left(), ",", null, true);
        } else {
            filterDimensionsArray = filterDimensions.right();
        }

        // Dimensions for filter
        List<String> filterDims = new ArrayList<>();
        for (String dimension : filterDimensionsArray) {
            // TODO: Проверить названия полей
            if (dimension.equalsIgnoreCase("period") || dimension.equalsIgnoreCase("start_date") || dimension.equalsIgnoreCase("end_date")) {
                continue;
            }

            filterDims.add(dimension);
        }

        HRBD_RegistryDescription registryDescription = new HRBD_RegistryDescription(registryName, excludeUnused);
        registryDescription.setFilterDimensions(filterDims);

        return registryDescription;
    }
}
