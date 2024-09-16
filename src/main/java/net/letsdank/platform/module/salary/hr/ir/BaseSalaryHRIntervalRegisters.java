package net.letsdank.platform.module.salary.hr.ir;

import net.letsdank.platform.module.salary.hr.base.SalaryHRBaseDataset;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_RegistryDescription;
import net.letsdank.platform.module.salary.hr.pr.HRPR_SQLQueryBuild;
import net.letsdank.platform.module.salary.hr.pr.context.CreateTTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescription;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_QueryDescriptionOperator;
import net.letsdank.platform.module.salary.hr.pr.description.HRPR_RegistryQueriesDescriptionPacket;
import net.letsdank.platform.module.salary.hr.pr.filter.CreateTTRegistryNameFilter;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterValueList;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_CastPeriodOption;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodFieldDescription;
import net.letsdank.platform.module.salary.hr.pr.period.HRPR_PeriodMultiplicity;
import net.letsdank.platform.utils.data.Either;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Alias: ИнтервальныеРегистрыБЗК
// БЗК - Базовая Зарплата Кадры
public class BaseSalaryHRIntervalRegisters {

    public static String getIntervalRegistryName(String registryName) {
        return registryName + "_interval";
    }

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter) {
        addQueryTTTransactionByIntervalRegistryName(queriesDescriptionPacket, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                                   TTRegistryNameBuildContext buildContext) {
        addQueryTTTransactionByIntervalRegistryName(queriesDescriptionPacket, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияИмяИнтервальногоРегистра
    public static void addQueryTTTransactionByIntervalRegistryName(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                                   String registryName, boolean onlyDistrict, CreateTTRegistryNameFilter<HRPR_FilterValueList> filter,
                                                                   TTRegistryNameBuildContext buildContext, String resultTTName) {
        String parameterNamePostfix = resultTTName;
        if (parameterNamePostfix == null) {
            parameterNamePostfix = registryName + "_transactions";
        }

        if (buildContext == null) {
            buildContext = new CreateTTRegistryNameBuildContext();
        }

        String intervalRegistryName = getIntervalRegistryName(registryName);

        HRBD_RegistryDescription registryDescription = SalaryHRBaseDataset.getInfoRegistryDescription(registryName,
                Either.right(filter.getFilterDimensions()), buildContext.isExcludeUnusedFields());

        boolean formFromPeriodicityDay = buildContext.isFormFromPeriodicityDay(registryDescription);
        boolean includeRecordsAtPeriodStart = buildContext.isIncludeRecordsAtPeriodStart(registryDescription);

        String templateQuery = "SELECT " +
                " INTO vt_registry_transactions " +
                "   &tmpl_registrar AS registrar," +
                "   &tmpl_record_registrar AS record_registrar," +
                "   &tmpl_registry_start_date_given AS period," +
                "   &tmpl_return_event_period AS return_event_period," +
                "   &tmpl_is_return_event AS is_return_event," +
                "   info_registry.start_date AS start_date," +
                "   info_registry.end_date AS end_date," +
                "   &tmpl_period_of_record AS period_of_record," +
                "   &tmpl_empty_interval AS empty_interval," +
                "   info_registry.year AS year," +
                "   &tmpl_dimensions AS tmpl_dimensions," +
                "   &tmpl_resources AS tmpl_resources," +
                "   &tmpl_requests AS tmpl_requests" +
                "FROM vt_date_dimensions AS date_dimensions" +
                "   INNER JOIN #info_registry AS info_registry ON" +
                "     (info_registry.start_date >= &filter_start_date_given_)" +
                "     AND (info_registry.end_date <= &filter_end_date_given_)" +
                "     AND (&tmpl_filter_dimensions_join_conditions)" +
                "   LEFT JOIN #info_registry AS info_registry_replacement ON" +
                "     (info_registry.previous_record_date = info_registry_replacement.start_date)" +
                "     AND (info_registry.record_registrar <> info_registry_replacement.record_registrar)" +
                "     AND (info_registry.period_of_record = info_registry.start_date OR info_registry_replacement.record_registrar IN (&tmpl_excluded_registrar))" +
                "     AND (&tmpl_dimensions_join_conditions)";

        String tmplConditionsExcludedRegistrar = " CASE" +
                "  WHEN info_registry.start_date < &filter_start_date_given_ THEN TRUE" +
                "  WHEN info_registry.record_registrar IN (&tmpl_excluded_registrar) AND info_registry_replacement.period_of_record IS NULL THEN FALSE" +
                "  WHEN info_registry.period_of_record = info_registry.start_date" +
                "       AND info_registry.record_registrar IN (&tmpl_excluded_registrar)" +
                "       AND info_registry_replacement.end_date + interval '1 second' = info_registry.start_date THEN FALSE" +
                "  WHEN info_registry.period_of_record <> info_registry.start_date AND info_registry_replacement.record_registrar IN (&tmpl_excluded_registrar) THEN FALSE" +
                "  ELSE TRUE" +
                " END";

        String periodicityDayCondition = "info_registry.end_date >= date_trunc('day', info_registry.start_date) + interval '1 day - 1 second'";

        String expressionIsReturnEvent = "CASE" +
                "  WHEN info_registry.period_of_record = info_registry.start_date THEN FALSE" +
                "  ELSE TRUE" +
                " END";

        String templateSelectingResource;
        if (buildContext.isExcludedRegisters()) {
            templateSelectingResource = "CASE" +
                    "  WHEN info_registry.record_registrar IN (&tmpl_excluded_registrar) THEN info_registry_replacement.resource_" +
                    "  ELSE info_registry.resource_" +
                    " END";

            expressionIsReturnEvent = expressionIsReturnEvent.replace("info_registry.period_of_record", templateSelectingResource)
                    .replace("resource_", "period_of_record");
        } else {
            templateSelectingResource = "info_registry.resource_";
        }

        HRPR_QueryDescription queryDescription = HRPR_SQLQueryBuild.getQueryDescriptionByText(templateQuery);
        queriesDescriptionPacket.getDataQueries().add(Either.right(queryDescription));

        queryDescription.setDistinct(true);
        queryDescription.setTableToPlace(resultTTName);

        HRPR_QueryDescriptionOperator queryOperator = queryDescription.getOperators().get(0);

        HRPR_FilterUsageDescription filterUsageDescription = new HRPR_FilterUsageDescription();
        filterUsageDescription.initialize(filter, registryDescription, "start_date, end_date", queryOperator, parameterNamePostfix, false);

        if (!filterUsageDescription.isFilterAsTT()) {
            queryOperator.replaceLeadingTableInJoin("info_registry_replacement", "info_registry");
        }

        queryOperator.replaceTable("info_registry", getIntervalRegistryName(registryName));
        queryOperator.replaceTable("info_registry_replacement", getIntervalRegistryName(registryName));

        HRPR_PeriodFieldDescription registryStartDateGiven = new HRPR_PeriodFieldDescription("start_date", "info_registry");
        HRPR_PeriodFieldDescription registryStartDateGivenToDayEnd = new HRPR_PeriodFieldDescription("start_date", "info_registry");

        HRPR_PeriodFieldDescription filterStartDateGiven = filterUsageDescription.addPeriodOfFilter("start_date", "start_date_given");
        HRPR_PeriodFieldDescription filterEndDateGiven = filterUsageDescription.addPeriodOfFilter("end_date", "end_date_given");
        HRPR_PeriodFieldDescription filterEndDate = filterUsageDescription.addPeriodOfFilter("end_date", "end_date");
        HRPR_PeriodFieldDescription filterDateOfSlice = filterUsageDescription.addPeriodOfFilter("start_date", "date_of_slice");
        HRPR_PeriodFieldDescription filterDateOfSliceGiven = filterUsageDescription.addPeriodOfFilter("start_date", "date_of_slice_given");

        filterEndDateGiven.setEmptyValueAsMaximum(true);

        HRPR_PeriodMultiplicity offsetMultForSlice = HRPR_PeriodMultiplicity.SECOND;
        if (formFromPeriodicityDay) {
            registryStartDateGiven.setMultiplicity(HRPR_PeriodMultiplicity.SECOND);
            registryStartDateGiven.setCastPeriodOption(HRPR_CastPeriodOption.STARTPERIOD);

            filterStartDateGiven.setMultiplicity(HRPR_PeriodMultiplicity.DAY);
            filterStartDateGiven.setCastPeriodOption(HRPR_CastPeriodOption.STARTPERIOD);

            filterEndDateGiven.setMultiplicity(HRPR_PeriodMultiplicity.DAY);
            filterEndDateGiven.setCastPeriodOption(HRPR_CastPeriodOption.ENDPERIOD);

            registryStartDateGivenToDayEnd.setMultiplicity(HRPR_PeriodMultiplicity.DAY);
            registryStartDateGivenToDayEnd.setCastPeriodOption(HRPR_CastPeriodOption.ENDPERIOD);

            filterDateOfSlice.setMultiplicity(HRPR_PeriodMultiplicity.DAY);
            filterDateOfSlice.setCastPeriodOption(HRPR_CastPeriodOption.STARTPERIOD);

            filterDateOfSliceGiven.setMultiplicity(HRPR_PeriodMultiplicity.DAY);
            filterDateOfSliceGiven.setCastPeriodOption(HRPR_CastPeriodOption.ENDPERIOD);

            offsetMultForSlice = HRPR_PeriodMultiplicity.DAY;
            queryOperator.addCondition(periodicityDayCondition);
        }

        String periodFieldText;
        if (includeRecordsAtPeriodStart) {
            // Append period join condition with start of period record condition
            String templateRecordConditionByStartPeriod = "OR &registry_start_date_given_ <= &filter_date_of_slice_given_" +
                    " AND info_registry.end_date >= &filter_date_of_slice_given_";

            filterUsageDescription.setTemplateJoinCondition("(" + filterUsageDescription.getTemplateJoinCondition() +
                    "\n" + templateRecordConditionByStartPeriod + ")");

            // For records at start of period, "period" field value will count as "start_date" filter parameter
            periodFieldText = "CASE" +
                    "  WHEN info_registry.start_date <= &filter_date_of_slice_ THEN &filter_date_of_slice_" +
                    "  ELSE &registry_start_date_given_" +
                    " END";

            periodFieldText = periodFieldText.replace("&filter_date_of_slice_", filterStartDateGiven.getPeriodFieldExpression())
                    .replace("&registry_start_date_given_", registryStartDateGiven.getPeriodFieldExpression());

            filterStartDateGiven.setOffset(1);
            filterStartDateGiven.setMultiplicity(offsetMultForSlice);

            filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_date_of_slice_given_", filterDateOfSliceGiven);
            filterUsageDescription.putPeriodExpressionInJoinTemplate("&registry_start_date_given_", registryStartDateGiven);
        } else {
            periodFieldText = registryStartDateGiven.getPeriodFieldExpression();
        }

        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_start_date_given_", filterStartDateGiven);
        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_end_date_", filterEndDate);
        filterUsageDescription.putPeriodExpressionInJoinTemplate("&filter_end_date_given_", filterEndDateGiven);
        filterUsageDescription.putPeriodExpressionInJoinTemplate("&registry_start_date_given_to_day_end_", registryStartDateGivenToDayEnd);

        List<String> dimensionJoinConditions = new ArrayList<>();
        for (String dimension : registryDescription.getDimensions()) {
            queryDescription.addField(0, "info_registry." + dimension, dimension);
            String conditionText = "info_registry." + dimension + " = info_registry_replacement." + dimension;
            dimensionJoinConditions.add(conditionText);
        }

        if (buildContext.isExcludedRegisters()) {
            // TODO: implement
        } else {
            queryOperator.deleteTable("info_registry_replacement");
        }

        queryDescription.addField(0, periodFieldText, "period");

        Map<String, Object> expressionsMap = new HashMap<>();

        queryDescription.addField(0, expressionIsReturnEvent, "is_return_event");

        String fieldExpressionReturnEventPeriod = templateSelectingResource.replace("resource_", "valid_to");
        queryDescription.addField(0, fieldExpressionReturnEventPeriod, "return_event_period");
        expressionsMap.put("return_event_period".toUpperCase(), fieldExpressionReturnEventPeriod);

        String fieldExpressionPeriodOfRecord = templateSelectingResource.replace("resource_", "start_date");
        queryDescription.addField(0, fieldExpressionPeriodOfRecord, "period_of_record");
        expressionsMap.put("period_of_record".toUpperCase(), fieldExpressionPeriodOfRecord);

        String fieldExpressionRegistrar = templateSelectingResource.replace("resource_", "event_registrar");
        queryDescription.addField(0, fieldExpressionRegistrar, "registrar");
        expressionsMap.put("registrar".toUpperCase(), fieldExpressionRegistrar);

        queryDescription.addField(0, fieldExpressionRegistrar, "event_registrar");
        expressionsMap.put("registrar".toUpperCase(), fieldExpressionRegistrar);

        String fieldExpressionRecordRegistrar = templateSelectingResource.replace("resource_", "record_registrar");
        queryDescription.addField(0, fieldExpressionRecordRegistrar, "record_registrar");
        expressionsMap.put("record_registrar".toUpperCase(), fieldExpressionRecordRegistrar);

        String fieldExpressionEmptyInterval = templateSelectingResource.replace("resource_", "empty_interval");
        queryDescription.addField(0, fieldExpressionEmptyInterval, "empty_interval");
        expressionsMap.put("empty_interval".toUpperCase(), fieldExpressionEmptyInterval);

        for (String resource : registryDescription.getResources()) {
            String fieldExpressionResource = templateSelectingResource.replace("resource_", resource);
            queryDescription.addField(0, fieldExpressionResource, resource);
            expressionsMap.put(resource.toUpperCase(), fieldExpressionResource);
        }

        for (String resource : registryDescription.getReturnedResources()) {
            String fieldExpressionResource = templateSelectingResource.replace("resource_", resource);
            queryDescription.addField(0, fieldExpressionResource, resource);
            expressionsMap.put(resource.toUpperCase(), fieldExpressionResource);
        }

        for (String request : registryDescription.getRequests()) {
            String fieldExpressionRequest = templateSelectingResource.replace("resource_", request);
            queryDescription.addField(0, fieldExpressionRequest, request);
            expressionsMap.put(request.toUpperCase(), fieldExpressionRequest);
        }

        queriesDescriptionPacket.putFilter(filter, filterUsageDescription, buildContext);

        int parametersCount = 1;
        queryOperator.putConditionInRegistry(buildContext.getFilters(), queriesDescriptionPacket.getParameters(), resultTTName, parametersCount,
                null, null, false);

        String predicateConditionByStartPeriod = "info_registry.start_date >= " + filterStartDateGiven.getPeriodFieldExpression() + " OR ";
        // TODO: Нужно приводить каст или должен быть именно такой тип в аргументе?
        queryOperator.putConditionInRegistry(((CreateTTRegistryNameBuildContext) buildContext).getFiltersAtPeriodStart(),
                queriesDescriptionPacket.getParameters(), parameterNamePostfix, parametersCount, predicateConditionByStartPeriod, null, false);

        queryDescription.putAdditionalFieldsByFUD(0, filterUsageDescription);
        queriesDescriptionPacket.addConstantFields(queryDescription, buildContext.getConstantFields(), resultTTName);

        queryDescription.putFieldAliases(buildContext);
        queryDescription.addIndexFields(buildContext.getIndexingBy());
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter) {
        addQueryTTTransactionByFirstRegistry(queriesDescriptionPacket, registryName, onlyDistrict, filter, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter,
                                                            TTRegistryNameBuildContext buildContext) {
        addQueryTTTransactionByFirstRegistry(queriesDescriptionPacket, registryName, onlyDistrict, filter, buildContext, null);
    }

    // Alias: ДобавитьЗапросВТДвиженияПервичныйРегистр
    public static void addQueryTTTransactionByFirstRegistry(HRPR_RegistryQueriesDescriptionPacket queriesDescriptionPacket,
                                                            String registryName, boolean onlyDistrict, Object filter,
                                                            TTRegistryNameBuildContext buildContext, String resultTTName) {
        // TODO: Implement
    }
}
