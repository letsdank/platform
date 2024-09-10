package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.base.SalaryHRBaseDataset;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.ir.BaseSalaryHRIntervalRegisters;
import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.filter.FilterUsageDescription;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_RegistryQueriesDescriptionPacket;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescription;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionOperator;

// Формирование запросов к регистрам
public class HRPR_RegistryQuery {
    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(HRPR_RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(HRPR_RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter, TTRegistryNameBuildContext buildContext) {
        addQueryTTRegistryName(packet, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТИмяРегистра
    static void addQueryTTRegistryName(HRPR_RegistryQueriesDescriptionPacket packet, String registryName,
                                       boolean onlyDistrict, Object filter,
                                       TTRegistryNameBuildContext buildContext, String resultTTName) {
        if (buildContext instanceof CreateTTRegistryNameBuildContext) {
            ((CreateTTRegistryNameBuildContext) buildContext).setTtNameAtPeriodStart("");
        }

        String ttNameAvailableRecords = HRPR_Utils.getAdditionalTTNameAvailableRecords(registryName, buildContext);
        if (HRPR_Utils.isAvailableIntervalRegistry(registryName)) {
            if (HRPR_QueryBuildOptions.useFirstRegistry(buildContext, registryName)) {
                BaseSalaryHRIntervalRegisters.addQueryTTTransactionByFirstRegistry(packet, registryName,
                        onlyDistrict, filter, buildContext);
            } else {
                BaseSalaryHRIntervalRegisters.addQueryTTTransactionByIntervalRegistryName(packet, registryName,
                        onlyDistrict, filter, buildContext, resultTTName);
            }
            buildContext.setExcludedRegisters(false);
            return;
        }

        addQueryTTAvailableRecordsByRegistryName(packet, registryName, onlyDistrict, filter, ttNameAvailableRecords, buildContext);
        addQueryTTRegistryTable(packet, registryName, onlyDistrict, filter, ttNameAvailableRecords, buildContext, resultTTName);

        // TODO: implement
    }

    // Alias: ДобавитьЗапросВТДоступныеЗаписиИмяРегистра
    static void addQueryTTAvailableRecordsByRegistryName(HRPR_RegistryQueriesDescriptionPacket packet, String registryName,
                                                         boolean onlyDistrict, Object filter, String ttNameAvailableRecords) {
        addQueryTTAvailableRecordsByRegistryName(packet, registryName, onlyDistrict, filter, ttNameAvailableRecords, null);
    }

    // Alias: ДобавитьЗапросВТДоступныеЗаписиИмяРегистра
    static void addQueryTTAvailableRecordsByRegistryName(HRPR_RegistryQueriesDescriptionPacket packet, String registryName,
                                                         boolean onlyDistrict, Object filter, String ttNameAvailableRecords,
                                                         TTRegistryNameBuildContext buildContext) {
        String parameterNamePostfix = ttNameAvailableRecords;

        if (buildContext == null) {
            buildContext = new CreateTTRegistryNameBuildContext();
        }

        HRBD_RegistryDescription registryDescription = SalaryHRBaseDataset.getInfoRegistryDescription(registryName,
                filter.getFilterDimensions(), buildContext.isExcludeUnusedFields());
        boolean includeRecordsInPeriodStart = HRPR_QueryBuildOptions.isIncludeRecordsAtPeriodStart(buildContext, registryDescription);

        // TODO: Full hardcore - try to separate it
        String querySelectRegistryRecords = "SELECT DISTINCT" +
                " INTO vt_available_records " +
                "  &tmpl_dimension AS selected_fields, " +
                "  &tmpl_resources as resources, " +
                "  &tmpl_requests as requests, " +
                "  &tmpl_standard_requests as standard_requests, " +
                "  &tmpl_period as period, " +
                "  &tmpl_period_record as period_record, " +
                "  &tmpl_record_of_period as record_of_period, " +
                "  &tmpl_valid_to as valid_to, " +
                "  &tmpl_return_record as return_record " +
                "  FROM vt_dimensions_date AS dimensions_date " +
                "  INNER JOIN #info_registry as info_registry " +
                "  ON (&tmpl_condition_join_period)" +
                "    AND (&tmpl_condition_join_dimension)";

        String templateFieldReturnRecord = "CASE WHEN info_registry.valid_to = NULL THEN FALSE " +
                "  WHEN info_registry.valid_to <= &filter_end_date_given_ THEN TRUE" +
                "  ELSE FALSE END";

        String templateFieldRecordOfPeriod = "(info_registry.period >= &filter_start_date_" +
                "  AND (info_registry.period <= &filter_end_date_given_)";

        String templateConditionJoinPeriod = "(info_registry.period >= &filter_start_date_ " +
                "  AND (info_registry.period <= &filter_end_date_given_)" +
                (
                        registryDescription.isHasReturnEvents()
                        ?
                                " OR " +
                                " info_registry.valid_to >= &filter_start_date_" +
                                "   AND (info_registry.valid_to <= &filter_end_date_given_)" +
                                "   AND info_registry.valid_to <> NULL))"
                        : ")"
                );

        HRPR_QueryDescription queryDescription = HRPR_SQLQueryBuild.getQueryDescriptionByText(querySelectRegistryRecords);
        packet.getDataQueries().add(Either.right(queryDescription));

        queryDescription.setDistinct(onlyDistrict);
        queryDescription.setTableToPlace(ttNameAvailableRecords);

        HRPR_QueryDescriptionOperator queryOperator = queryDescription.getOperators().get(0);

        FilterUsageDescription filterUsageDescription = HRPR_QueryFilter.getFilterUsageDescription();
        HRPR_QueryFilter.initializeFilterUsage(filterUsageDescription, filter, registryDescription, "date_from, date_to",
                queryOperator, parameterNamePostfix, false);

        filterUsageDescription.setTemplateConditionJoinPeriod(templateConditionJoinPeriod); // TODO:

        HRPR_SQLQuery.replaceTableInQueryOperator(queryOperator, "info_registry", registryName);

        Object filterDateStart = addFieldDescriptionFilterPeriod(filterUsageDescription, "date_from", "filter_date_from");
        Object filterDateEnd = addFieldDescriptionFilterPeriod(filterUsageDescription, "date_to", "filter_date_to");
        Object filterDateEndGiven = addFieldDescriptionFilterPeriod(filterUsageDescription, "date_to", "filter_date_to_given");

        // TODO: implement
    }
}
