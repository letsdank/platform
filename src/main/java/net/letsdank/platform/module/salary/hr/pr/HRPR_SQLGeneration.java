package net.letsdank.platform.module.salary.hr.pr;

import net.letsdank.platform.module.salary.hr.pr.entity.*;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaUnionType;

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
    static void performQueryDescriptionToStrings(List<String> strings, HRPR_QueryDescription description, boolean showCompositionElements) {
        int opIndex = 0;

        for (HRPR_QueryDescriptionOperator op : description.getOperators()) {
            strings.add("\n");

            if (opIndex != 0) {
                strings.add(op.getJoinType());
                strings.add("\n");
            }

            performQueryOpToStrings(description, op, strings, opIndex == 0, showCompositionElements);
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
    static void performQueryOpToStrings(HRPR_QueryDescription description, HRPR_QueryDescriptionOperator operator, List<String> strings,
                                        boolean isFirstOperator, boolean showCompositionElements) {
        strings.add("SELECT");

        if (isFirstOperator && description.isDistinct()) {
            strings.add(" DISTINCT");
        }

        performQueryOpModifierToStrings(description, operator, strings);
        performQueryOpFieldsToStrings(description, operator, strings);

        if (showCompositionElements && isFirstOperator) {
            performCompositionElementsToStrings(operator, strings);
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

        if (showCompositionElements) {
            performQueryOpCompositionElementsToStrings(operator, strings);
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
    static void performCompositionElementsToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getSelectedCompositionFields().isEmpty()) {
            strings.add("\n");
            strings.add("{SELECT"); // TODO: Это поддерживается силами pgsql?
            strings.add("\n");
            strings.add("\t");

            int fieldIndex = 0;
            for (HRPR_QueryDescriptionCompositionField field : operator.getSelectedCompositionFields()) {
                strings.add(field.getField());
                strings.add(" AS ");
                strings.add(field.getCompositionAlias());
                if (fieldIndex != operator.getSelectedCompositionFields().size() - 1) {
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
        for (Either<HRPR_QueryDescriptionNestedQuery, HRPR_QueryDescriptionTableQuery> table : operator.getTables()) {
            String tableAlias = table.isLeft() ? table.left().getAlias() : table.right().getAlias();
            if (operator.getJoins().stream().anyMatch(join -> join.getJoiningTable().equals(tableAlias))) {
                continue;
            }

            if (tableIndex != 0) {
                strings.add(",");
                strings.add("\n");
            }

            // TODO: Что-то сделать с этим
            if (table.isLeft()) {
                strings.add("(");
                performQueryDescriptionToStrings(strings, table.left().getQueryDescription(), false);
                strings.add(")");
            } else {
                strings.add(table.right().getName());
            }

            strings.add(" AS ");
            strings.add(tableAlias);

            List<HRPR_QueryDescriptionJoin> joins = new ArrayList<>(operator.getJoins().stream()
                    .filter(join -> join.getLeadingTable().equals(tableAlias))
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
        // TODO: Нужно делать проверку isRight()?
        HRPR_QueryDescriptionTableQuery joiningTableDescription = joiningQueryDescription.getSources().get(join.getJoiningTable()).right();
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
    static void performQueryOpCompositionElementsToStrings(HRPR_QueryDescriptionOperator operator, List<String> strings) {
        if (!operator.getCompositionFieldConditions().isEmpty()) {
            strings.add("\n");
            strings.add("{WHERE"); // TODO: Это поддерживается силами pgsql?
            strings.add("\n");
            strings.add("\t");

            int fieldConditionIndex = 0;
            for (String fieldCondition : operator.getCompositionFieldConditions()) {
                strings.add(fieldCondition);
                if (fieldConditionIndex != operator.getCompositionFieldConditions().size() - 1) {
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

    static String getUnionTypeAsString(QuerySchemaUnionType type) {
        if (type == QuerySchemaUnionType.UNION) return "UNION";
        return "UNION ALL";
    }
    
    public static final String COND_DELIMITER = "\n\tAND ";
    public static final String FIELD_DELIMITER = ",\n\t";
}
