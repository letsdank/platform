package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.base.entity.HRBD_FilterDescription;
import net.letsdank.platform.module.salary.hr.pr.HRPR_Utils;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.join.QuerySchemaJoinType;

import java.util.*;

@Getter
@Setter
public class HRPR_QueryDescriptionOperator {
    // Alias: НовыйОператорЗапроса
    private String joinType;
    private final List<String> selectedFields = new ArrayList<>();
    private boolean distinct = false;
    private Integer recordCount = 0;
    private final List<Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery>> tables = new ArrayList<>();
    private final List<HRPR_QueryDescriptionJoin> joins = new ArrayList<>();
    private final List<String> conditions = new ArrayList<>();
    private final List<String> having = new ArrayList<>();
    private final List<String> groups = new ArrayList<>();
    private final List<HRPR_QueryDescriptionCompositionField> selectedCompositionFields = new ArrayList<>();
    private final List<String> compositionFieldConditions = new ArrayList<>();
    private final Map<String, Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery>> sources = new HashMap<>();
    private final List<String> fieldAliases = new ArrayList<>();

    private final List<String> columns = new ArrayList<>();

    // Alias: ЗаменитьТаблицуВОператореЗапроса
    public void replaceTable(String replacementTableAlias, String newTableName) {
        Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery> tableDescription = sources.get(replacementTableAlias);
        if (tableDescription.isRight()) {
            tableDescription.right().setName(newTableName);
        }
    }

    // Alias: УдалитьТаблицуИзОператораЗапроса
    public void deleteTable(String deleteTableAlias) {
        Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery> tableDescription = sources.get(deleteTableAlias);
        sources.remove(deleteTableAlias);
        tables.remove(tableDescription);

        joins.removeIf(filter -> filter.getJoiningTable().equals(deleteTableAlias));
        joins.removeIf(filter -> filter.getLeadingTable().equals(deleteTableAlias));
    }


    // Alias: ДобавитьУсловие
    public void addCondition(String condition) {
        conditions.add(condition);
    }

    // Alias: ДобавитьУсловие
    public void addCondition(List<String> conditions) {
        this.conditions.addAll(conditions);
    }

    // Alias: ДобавитьУсловиеСоединения
    public void addJoinCondition(String joiningTableAlias, String conditions) {
        Optional<HRPR_QueryDescriptionJoin> joinDescription = joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        joinDescription.ifPresent(hrprQueryDescriptionJoin -> hrprQueryDescriptionJoin.getConditions().add("(" + conditions + ")"));
    }

    // Alias: ДобавитьУсловиеСоединения
    public void addJoinCondition(String joiningTableAlias, List<String> conditions) {
        Optional<HRPR_QueryDescriptionJoin> joinDescription = joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        joinDescription.ifPresent(join ->
                join.getConditions().addAll(
                        conditions
                            .stream()
                            .map(condition -> "(" + condition + ")")
                            .toList()
        ));
    }

    // Alias: ДобавитьГруппировку
    public void addGroup(String groupExpression) {
        groups.add(groupExpression);
    }

    // Alias: ВыраженияПолейОператораЗапроса
    public Map<String, String> getFieldExpressions() {
        Map<String, String> result = new HashMap<>();
        for (int fieldIndex = 0; fieldIndex < selectedFields.size(); fieldIndex++) {
            result.put(fieldAliases.get(fieldIndex).toLowerCase(), selectedFields.get(fieldIndex));
        }

        return result;
    }

    // Alias: ОписаниеСоединенияПоПрисоединяемойТаблице
    public HRPR_QueryDescriptionJoin getJoinDescriptionByJoiningTable(String joiningTableAlias) {
        return joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst()
                .orElse(null);
    }


    // Alias: ОписаниеСоединенияСТаблицейФильтра
    public HRPR_QueryDescriptionJoin getJoinDescriptionWithTableFilter(String filterTableAlias, String registryTableAlias) {
        List<HRPR_QueryDescriptionJoin> foundJoins = joins.stream()
                .filter(join -> join.getLeadingTable().equals(filterTableAlias) && join.getJoiningTable().equals(registryTableAlias))
                .toList();

        if (foundJoins.isEmpty()) {
            foundJoins = joins.stream()
                    .filter(join -> join.getLeadingTable().equals(registryTableAlias) && join.getJoiningTable().equals(filterTableAlias))
                    .toList();
        }

        if (foundJoins.size() == 1) {
            return foundJoins.get(0);
        }

        return null;
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> parameters, String parameterPostfix) {
        putConditionInRegistry(filters, parameters, parameterPostfix, 1);
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> parameters, String parameterPostfix,
                                       int parametersCount) {
        putConditionInRegistry(filters, parameters, parameterPostfix, parametersCount, "info_registry");
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> parameters, String parameterPostfix,
                                       int parametersCount, String predicate) {
        putConditionInRegistry(filters, parameters, parameterPostfix, parametersCount, predicate, "info_registry");
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> parameters, String parameterPostfix,
                                       int parametersCount, String predicate, String registryAlias) {
        putConditionInRegistry(filters, parameters, parameterPostfix, parametersCount, predicate, registryAlias, true);
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> parameters, String parameterPostfix,
                                       int parametersCount, String predicate, String registryAlias, boolean byExcludingRegistrars) {
        putConditionInRegistry(filters, parameters, parameterPostfix, parametersCount, predicate, registryAlias, byExcludingRegistrars, false);
    }

    // Alias: УстановитьОтборВОператорЗапросаДанныхРегистра
    public void putConditionInRegistry(List<HRBD_FilterDescription> filters, Map<String, Object> queryParameters, String parameterPostfix,
                                       int parametersCount, String predicate, String registryAlias, boolean byExcludingRegistrars,
                                       boolean byJoinCondition) {
        if (filters == null) {
            return;
        }

        HRPR_QueryDescriptionJoin joinDescription = getJoinDescriptionWithTableFilter("dates_dimensions", registryAlias);

        List<String> receiverConditions = null;
        if (joinDescription != null && byJoinCondition) {
            receiverConditions = joinDescription.getConditions();
        }

        Map<String, String> fieldExpressions = getFieldExpressions();

        for (HRBD_FilterDescription filter : filters) {
            if (isConditionByExcludingRegistrars(filter) && !byExcludingRegistrars) {
                continue;
            }

            String parameterDescription;
            String filterText;
            if (filter.rightValue() instanceof String) {
                parameterDescription = filter.rightValue().toString();
            } else {
                String parameterName = HRPR_Utils.getUniqueQueryParameterName(parameterPostfix, parametersCount);
                parameterDescription = "&" + parameterName;
                queryParameters.put(parameterName, filter.rightValue());
            }

            if (filter.relativePath() == null || filter.relativePath()) {
                String leftValueExpression = fieldExpressions.get(filter.leftValue().toLowerCase());
                if (leftValueExpression == null && registryAlias != null) {
                    leftValueExpression = registryAlias + "." + filter.leftValue();
                }

                filterText = predicate + leftValueExpression + " " + filter.compareType() + "(" + parameterDescription + ")";
            } else {
                filterText = predicate + filter.leftValue() + " " + filter.compareType() + " " + parameterDescription + " ";
            }

            if (receiverConditions == null) {
                addCondition(filterText);
            } else {
                receiverConditions.add(filterText);
            }
        }
    }

    // Alias: ЭтоЭлементОтбораПоИсключаемомуРегистратору
    // TODO: Перенести
    public boolean isConditionByExcludingRegistrars(HRBD_FilterDescription filter) {
        return filter.leftValue().equalsIgnoreCase("registrar") &&
                (filter.compareType().equalsIgnoreCase("<>") || filter.compareType().equalsIgnoreCase("NOT IN"));
    }

    // Alias: ВыражениеПоляПоПсевдониму
    public String getFieldExpressionByAlias(String fieldAlias) {
        for (int fieldIndex = 0; fieldIndex < selectedFields.size(); fieldIndex++) {
            if (fieldAliases.get(fieldIndex).equals(fieldAlias)) {
                return selectedFields.get(fieldIndex);
            }
        }

        return null;
    }

    // Alias: ЗаменитьВедущуюТаблицуВСоединении
    public void replaceLeadingTableInJoin(String joiningTableAlias, String newLeadingTableAlias) {
        Optional<HRPR_QueryDescriptionJoin> joinDescription = joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        joinDescription.ifPresent(join -> join.setLeadingTable(newLeadingTableAlias));
    }

    // Alias: ОчиститьУсловияСоединения
    public void clearJoinConditions(String joiningTableAlias) {
        Optional<HRPR_QueryDescriptionJoin> joinDescription = joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        joinDescription.ifPresent(join -> join.getConditions().clear());
    }

    // Alias: УстановитьТипСоединения
    public void setJoinType(String joiningTableAlias, String joinType) {
        Optional<HRPR_QueryDescriptionJoin> joinDescription = joins.stream()
                .filter(join -> join.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        joinDescription.ifPresent(join -> join.setJoinType(joinType));
    }
}
