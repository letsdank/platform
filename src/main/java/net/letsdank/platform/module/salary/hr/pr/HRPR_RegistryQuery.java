package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.base.SalaryHRBaseDataset;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.ir.BaseSalaryHRIntervalRegisters;
import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_CastPeriodOption;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodFieldDescription;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_RegistryQueriesDescriptionPacket;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodMultiplicity;
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

        HRPR_FilterUsageDescription filterUsageDescription = new HRPR_FilterUsageDescription();
        filterUsageDescription.initialize(filter, registryDescription, "date_from, date_to",
                queryOperator, parameterNamePostfix, false);

        filterUsageDescription.setTemplateConditionJoinPeriod(templateConditionJoinPeriod); // TODO:

        queryOperator.replaceTable("info_registry", registryName);

        HRPR_PeriodFieldDescription filterDateStart = filterUsageDescription.addPeriodOfFilter("date_from", "filter_date_from");
        HRPR_PeriodFieldDescription filterDateEnd = filterUsageDescription.addPeriodOfFilter("date_to", "filter_date_to");
        HRPR_PeriodFieldDescription filterDateEndGiven = filterUsageDescription.addPeriodOfFilter("date_to", "filter_date_to_given");

        HRPR_PeriodFieldDescription periodRegistry = new HRPR_PeriodFieldDescription("period", "info_registry");

        filterDateEndGiven.setEmptyValueAsMaximum(true);

        HRPR_PeriodMultiplicity multiplicity = HRPR_PeriodMultiplicity.getMultiplicity(registryDescription, buildContext);
        periodRegistry.setMultiplicity(multiplicity);
        filterDateEndGiven.setMultiplicity(multiplicity);
        filterDateEndGiven.setCastPeriodOption(HRPR_CastPeriodOption.ENDPERIOD);

        if (includeRecordsInPeriodStart) {
            filterDateStart.setMultiplicity(multiplicity);
            filterDateStart.setOffset(1);
        } else if (HRPR_QueryBuildOptions.isFormFromPeriodicityDay(buildContext, registryDescription)) {
            filterDateStart.setMultiplicity(multiplicity);
        }

        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_start_date_", filterDateStart);
        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_end_date_", filterDateEnd);
        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_end_date_given_", filterDateEndGiven);

        queryDescription.addField(0, periodRegistry.getPeriodFieldExpression(), "period");
        queryDescription.addField(0, "info_registry.period", "period_record");

        String fieldRecordOfPeriod = templateFieldRecordOfPeriod.replace("&filter_start_date_", filterDateStart.getPeriodFieldExpression());
        fieldRecordOfPeriod = fieldRecordOfPeriod.replace("&filter_end_date_given_", filterDateEndGiven.getPeriodFieldExpression());

        queryDescription.addField(0, fieldRecordOfPeriod, "record_of_period");

        if (registryDescription.isHasReturnEvents()) {
            String fieldReturnRecord = templateFieldReturnRecord.replace("&filter_end_date_", filterDateEnd.getPeriodFieldExpression());
            fieldReturnRecord = fieldReturnRecord.replace("&filter_end_date_given_", filterDateEndGiven.getPeriodFieldExpression());

            queryDescription.addField(0, fieldReturnRecord, "return_record");
            queryDescription.addField(0, "info_registry.valid_to", "valid_to");
        }

        for (String dimension : registryDescription.getDimensions()) {
            queryDescription.addField(0, "info_registry." + dimension, dimension);
        }

        for (String resource : registryDescription.getReturnedResources()) {
            queryDescription.addField(0, "info_registry." + resource, resource);
            String returnResourceName = resource + "_on_end";
            queryDescription.addField(0, "info_registry." + returnResourceName, returnResourceName);
        }

        for (String resource : registryDescription.getResources()) {
            queryDescription.addField(0, "info_registry." + resource, resource);
        }

        for (String request : registryDescription.getRequests()) {
            queryDescription.addField(0, "info_registry." + request, request);
        }

        for (String request : registryDescription.getStandardRequests()) {
            queryDescription.addField(0, "info_registry." + request, request);
        }

        packet.putFilter(filter, filterUsageDescription, buildContext);
        // TODO: implement
    }
}
