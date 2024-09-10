package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    // Alias: ДобавитьГруппировку
    public void addGroup(String groupExpression) {
        groups.add(groupExpression);
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
}
