package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.*;
import net.letsdank.platform.utils.platform.sql.description.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Генерация текста запроса по модели
public class HRPR_SQLGeneration {
    // Alias: ОписаниеЗапросаВМассивСтрок
    static void performQueryDescriptionToStrings(List<String> strings, HRPR_QueryDescription description) {
        performQueryDescriptionToStrings(strings, description, true);
    }

    // Alias: ОписаниеЗапросаВМассивСтрок
    static void performQueryDescriptionToStrings(List<String> strings, HRPR_QueryDescription description, boolean showReportElements) {
        int opIndex = 0;

        for (HRPR_QueryDescriptionOperator op : description.getOperators()) {
            strings.add("\n");

            if (opIndex != 0) {
                strings.add(op.getJoinType());
                strings.add("\n");
            }

            performQueryOpToStrings(description, op, strings, opIndex == 0, showReportElements);
            opIndex++;
        }

        performOrderQueryToStrings(description, strings);
        performIndexQueryToStrings(description, strings);
    }

    // Alias: УпорядочиваниеОписанияЗапросаВМассивСтрок
    static void performOrderQueryToStrings(HRPR_QueryDescription description, List<String> strings) {
        int sortElementIndex = 0;

        if (!description.getOrder().isEmpty()) {
            strings.add("\n");
            strings.add("\n");
            strings.add("ORDER BY ");
            strings.add("\n");
        }

        for (String orderElement : description.getOrder()) {
            strings.add(orderElement);

            if (sortElementIndex != description.getOrder().size() - 1) {
                strings.add(FIELD_DELIMITER);
            }

            sortElementIndex++;
        }
    }

    // Alias: ИндексированиеОписанияЗапросаВМассивСтрок
    static void performIndexQueryToStrings(HRPR_QueryDescription description, List<String> strings) {
        int indexElementIndex = 0;

        if (description.getTableToPlace() != null && !description.getIndexFields().isEmpty()) {
            strings.add("\n");
            strings.add("\n");
            strings.add("INDEX BY ");
            strings.add("\n");
            strings.add("\t");
        }

        for (String indexElement : description.getIndexFields()) {
            strings.add(indexElement);

            if (indexElementIndex != description.getIndexFields().size() - 1) {
                strings.add(FIELD_DELIMITER);
            }

            indexElementIndex++;
        }
    }

    // Alias: ОператорЗапросаВМассивСтрок
    static void performQueryOpToStrings(HRPR_QueryDescription description, HRPR_QueryDescriptionOperator operator, List<String> strings, boolean isFirstOperator, boolean showReportElements) {
        strings.add("SELECT");

        if (isFirstOperator && description.isDistinct()) {
            strings.add(" DISTINCT");
        }

        performQueryOpModifierToStrings(description, operator, strings);
        performQueryOpFieldsToStrings(description, operator, strings);

        if (showReportElements && isFirstOperator) {
            performReportElementsToStrings(operator, strings);
        }

        strings.add("\n");
        if (isFirstOperator && description.getTableToPlace() != null && !description.getTableToPlace().isEmpty()) {
            strings.add("INTO ");
            strings.add(description.getTableToPlace());
            strings.add("\n");
        }

        performQueryOpDataSourcesToStrings(operator, strings);
        strings.add("\n");

        performQueryOpConditionsToStrings(operator, strings);

        if (showReportElements) {
            performQueryOpReportElementsToStrings(operator, strings);
        }

        strings.add("\n");
        performQueryOpGroupToStrings(operator, strings);
        performQueryOpHavingToStrings(operator, strings);
    }

    // Alias: МодификаторыОператораЗапросаВМассивСтрок
    static void performQueryOpModifierToStrings(HRPR_QueryDescription description, HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (operator.isDistinct()) {
            strings.add(" DISTINCT");
        }

        if (operator.getRecordCount() != null) {
            strings.add(" FIRST " + operator.getRecordCount());
        }
    }

    // Alias: ПоляОператораЗапросаВМассивСтрок
    static void performQueryOpFieldsToStrings(HRPR_QueryDescription description, HRPR_QueryDescriptionOperator operator, List<String> strings) {
        strings.add("\n");
        strings.add("\t");

        int fieldIndex = 0;

        for (String field : operator.getSelectedFields()) {
            strings.add(field);
            strings.add(" AS ");
            strings.add(operator.getColumns().get(fieldIndex));
            if (fieldIndex!= operator.getSelectedFields().size() - 1) {
                // TODO: Здесь должен передаваться параметр fieldIndex, но в функции он не используется,
                // поэтому пока оставим как константу.
                strings.add(FIELD_DELIMITER);
            }
            fieldIndex++;
        }
    }

    // Alias: ПоляСКДОператораЗапросаВМассивСтрок
    static void performReportElementsToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getSelectedReportFields().isEmpty()) {
            strings.add("\n");
            strings.add("{SELECT"); // TODO: Это поддерживается силами pgsql?
            strings.add("\n");
            strings.add("\t");

            int fieldIndex = 0;
            for (HRPR_QueryDescriptionReportField field : operator.getSelectedReportFields()) {
                strings.add(field.getField());
                strings.add(" AS ");
                strings.add(field.getReportAlias());
                if (fieldIndex != operator.getSelectedReportFields().size() - 1) {
                    strings.add(FIELD_DELIMITER);
                }
                fieldIndex++;
            }
            strings.add("}");
        }
    }

    // Alias: ИсточникиДанныхОператораЗапросаВМассивСтрок
    static void performQueryOpDataSourcesToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getTables().isEmpty()) {
            strings.add("FROM");
            strings.add("\n");
            strings.add("\t");
        }

        int tableIndex = 0;
        for (HRPR_QueryDescriptionTable table : operator.getTables()) {
            if (operator.getJoins().stream().anyMatch(join -> join.getJoiningTable().equals(table.getAlias()))) {
                continue;
            }

            if (tableIndex != 0) {
                strings.add(",");
                strings.add("\n");
            }

            // TODO: Что-то сделать с этим
            if (table.getType() == "InnerJoinDescription") {
                strings.add("(");
                performQueryDescriptionToStrings(strings, table.getQueryDescription(), false);
                strings.add(")");
            } else {
                strings.add(table.getName());
            }

            strings.add(" AS ");
            strings.add(table.getAlias());

            List<HRPR_QueryDescriptionJoin> joins = new ArrayList<>(operator.getJoins().stream()
                    .filter(join -> join.getLeadingTable().equals(table.getAlias()))
                    .toList());
            joins.sort(Comparator.comparing(HRPR_QueryDescriptionJoin::getJoinOrder));

            for (HRPR_QueryDescriptionJoin join : joins) {
                performJoinToStrings(strings, join, operator);
            }

            tableIndex++;
        }
    }

    // Alias: УсловияОператораЗапросаВМассивСтрок
    static void performQueryOpConditionsToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getConditions().isEmpty()) {
            strings.add("\n");
            strings.add("WHERE");
            strings.add("\n");
            strings.add("\t");
        }

        int conditionIndex = 0;
        for (String condition : operator.getConditions()) {
            strings.add("(");
            strings.add(condition);
            strings.add(")");
            if (conditionIndex != operator.getConditions().size() - 1) {
                strings.add(FIELD_DELIMITER);
            }
            conditionIndex++;
        }
    }

    // Alias: ГруппировкаОператораЗапросаВМассивСтрок
    static void performQueryOpGroupToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getGroups().isEmpty()) {
            strings.add("\n");
            strings.add("GROUP BY");
            strings.add("\n");
            strings.add("\t");
        }

        int groupIndex = 0;
        for (String group : operator.getGroups()) {
            if (groupIndex != 0) {
                strings.add(FIELD_DELIMITER);
            }
            strings.add(group);
            groupIndex++;
        }
    }

    // Alias: СекцияИмеющиеОператораЗапросаВМассивСтрок
    static void performQueryOpHavingToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getHaving().isEmpty()) {
            strings.add("\n");
            strings.add("HAVING");
            strings.add("\n");
            strings.add("\t");
        }

        int index = 0;
        for (String having : operator.getHaving()) {
            if (index != 0) {
                strings.add(FIELD_DELIMITER);
            }

            strings.add(having);
            index++;
        }
    }

    // Alias: СоединениеВМассивСтрок
    static void performJoinToStrings(List<String> strings, HRPR_QueryDescriptionJoin join, HRPR_QueryDescriptionOperator joiningQueryDescription) {
        HRPR_QueryDescriptionTable joiningTableDescription = joiningQueryDescription.getSources().get(join.getJoiningTable());
        strings.add("\n\t");
        strings.add(join.getJoinType());
        strings.add(" JOIN ");
        strings.add(joiningTableDescription.getName());
        strings.add(" AS ");
        strings.add(joiningTableDescription.getAlias());

        List<HRPR_QueryDescriptionJoin> joins = new ArrayList<>(joiningQueryDescription.getJoins().stream()
                .filter(j -> j.getLeadingTable().equals(joiningTableDescription.getAlias())).toList());
        joins.sort(Comparator.comparing(HRPR_QueryDescriptionJoin::getJoinOrder));

        for (HRPR_QueryDescriptionJoin j : joins) {
            performJoinToStrings(strings, j, joiningQueryDescription);
        }

        strings.add("\n\tON ");
        int conditionIndex = 0;
        for (String condition : join.getConditions()) {
            strings.add("(");
            strings.add(condition);
            strings.add(")");
            if (conditionIndex != join.getConditions().size() - 1) {
                strings.add(COND_DELIMITER);
            }
            conditionIndex++;
        }

        if (join.getConditions().isEmpty()) {
            strings.add("TRUE");
        }
    }

    // Alias: ОтборыСКДОператораЗапросаВМассивСтрок
    static void performQueryOpReportElementsToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getReportFieldConditions().isEmpty()) {
            strings.add("\n");
            strings.add("{WHERE"); // TODO: Это поддерживается силами pgsql?
            strings.add("\n");
            strings.add("\t");

            int fieldConditionIndex = 0;
            for (String fieldCondition : operator.getReportFieldConditions()) {
                strings.add(fieldCondition);
                if (fieldConditionIndex != operator.getReportFieldConditions().size() - 1) {
                    strings.add(COND_DELIMITER);
                }
                fieldConditionIndex++;
            }

            strings.add("}");
        }
    }

    public static String getJoinConditionString(HRPR_QueryDescriptionOperator operator, String joiningTableAlias) {
        Optional<HRPR_QueryDescriptionJoin> join = operator.getJoins().stream()
                .filter(j -> j.getJoiningTable().equals(joiningTableAlias))
                .findFirst();

        return join.map(queryDescriptionJoin -> getConditionsString(queryDescriptionJoin.getConditions())).orElse(null);
    }

    static String getConditionsString(List<String> conditions) {
        if (conditions.isEmpty()) return "";
        if (conditions.size() == 1) return conditions.get(0);
        return String.join(COND_DELIMITER, conditions);
    }

    static String getJoinTypeString(QueryDescriptionUnionType type) {
        if (type == QueryDescriptionUnionType.UNION) return "UNION";
        return "UNION ALL";
    }
    
    public static final String COND_DELIMITER = "\n\tAND ";
    public static final String FIELD_DELIMITER = ",\n\t";
}
