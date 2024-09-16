package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.HRPR_InternalFunctions;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.module.salary.hr.pr.filter.HRPR_FilterUsageDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRPR_QueryDescription {
    // Alias: НовыйОписаниеЗапроса
    private boolean distinct = false;
    private String tableToPlace = "";
    private List<String> columns = new ArrayList<>();
    private final List<String> order = new ArrayList<>();
    private final List<HRPR_QueryDescriptionOperator> operators = new ArrayList<>();
    private final List<String> indexFields = new ArrayList<>();

    // Alias: ДобавитьПолеВОписаниеЗапроса
    public void addField(int queryOperatorIndex, String expression, String alias) {
        addField(queryOperatorIndex, expression, alias, true);
    }

    // Alias: ДобавитьПолеВОписаниеЗапроса
    public void addField(int queryOperatorIndex, String expression, String alias, boolean addFieldToComposition) {
        int fieldIndex = columns.indexOf(alias);

        if (fieldIndex == -1) {
            columns.add(alias);
            int queryIndex = 0;
            for (HRPR_QueryDescriptionOperator query : operators) {
                operators.get(queryIndex).getSelectedFields().add
                        (queryIndex == queryOperatorIndex ? expression : "NULL");

                query.getFieldAliases().add(alias);
                queryIndex++;
            }
        } else {
            operators.get(queryOperatorIndex).getSelectedFields().set(fieldIndex, expression);
        }

        if (addFieldToComposition && queryOperatorIndex == 0) {
            operators.get(queryOperatorIndex).getSelectedCompositionFields().add
                    (new HRPR_QueryDescriptionCompositionField(alias, null));
        }
    }

    // Alias: ДобавитьДополнительныеПоляПоОписаниюИспользованияФильтра
    public void putAdditionalFieldsByFUD(int opIndex, HRPR_FilterUsageDescription fud) {
        putAdditionalFieldsByFUD(opIndex, fud, false);
    }

    // Alias: ДобавитьДополнительныеПоляПоОписаниюИспользованияФильтра
    public void putAdditionalFieldsByFUD(int opIndex, HRPR_FilterUsageDescription fud, boolean addToGrouping) {
        for (String additionalFieldKey : fud.getAdditionalFields().keySet()) {
            String fieldExpression = HRPR_InternalFunctions.getAdditionalFieldExpressionByFUD(additionalFieldKey, fud);
            addField(opIndex, fieldExpression, additionalFieldKey);
            if (addToGrouping) {
                operators.get(opIndex).addGroup(fieldExpression);
            }
        }
    }

    // Alias: УдалитьКолонкуИзОписаниеЗапроса
    public void removeColumn(String columnName) {
        int fieldIndex = columns.indexOf(columnName);
        columns.remove(fieldIndex);

        for (HRPR_QueryDescriptionOperator query : operators) {
            query.getSelectedFields().remove(fieldIndex);
            query.getFieldAliases().remove(fieldIndex);
        }
    }

    // Alias: УстановитьПсевдонимыПолей
    public void putFieldAliases(TTRegistryNameBuildContext buildContext) {
        if (buildContext.getFieldAliasCorrespondence() == null && buildContext.getFieldAliasCorrespondenceForComposition() == null) {
            return;
        }

        if (buildContext.getFieldAliasCorrespondence() != null) {
            for (Map.Entry<String, String> alias : buildContext.getFieldAliasCorrespondence().entrySet()) {
                int columnIndex = columns.indexOf(alias.getKey());
                if (columnIndex != -1) {
                    columns.set(columnIndex, alias.getValue());

                    for (HRPR_QueryDescriptionOperator operator : operators) {
                        operator.getFieldAliases().set(columnIndex, alias.getValue());
                    }
                }
            }
        }

        for (HRPR_QueryDescriptionOperator operator : operators) {
            for (HRPR_QueryDescriptionCompositionField compositionField : operator.getSelectedCompositionFields()) {
                if (buildContext.getFieldAliasCorrespondence() != null
                        && buildContext.getFieldAliasCorrespondence().containsKey(compositionField.getField())) {
                    compositionField.setField(buildContext.getFieldAliasCorrespondence().get(compositionField.getField()));
                }

                if (buildContext.getFieldAliasCorrespondenceForComposition() != null
                        && buildContext.getFieldAliasCorrespondenceForComposition().containsKey(compositionField.getField())) {
                    compositionField.setCompositionAlias(buildContext.getFieldAliasCorrespondenceForComposition().get(compositionField.getField()));
                }
            }
        }
    }

    // Alias: УстановитьПсевдонимыПолейСКД
    public void putFieldAliasesForComposition(TTRegistryNameBuildContext buildContext) {
        if (buildContext.getFieldAliasCorrespondenceForComposition() == null) {
            return;
        }

        for (HRPR_QueryDescriptionOperator operator : operators) {
            for (HRPR_QueryDescriptionCompositionField compositionField : operator.getSelectedCompositionFields()) {
                if (buildContext.getFieldAliasCorrespondenceForComposition().containsKey(compositionField.getField())) {
                    compositionField.setCompositionAlias(buildContext.getFieldAliasCorrespondenceForComposition().get(compositionField.getField()));
                }
            }
        }
    }

    // Alias: ДобавитьПоляИндексированияВОписаниеЗапроса
    public void addIndexFields(List<String> indexFields) {
        if (indexFields == null) {
            return;
        }

        this.indexFields.addAll(indexFields);
    }

    // Alias: ДобавитьПоляУпорядочиванияВОписаниеЗапроса
    public void addOrder(List<String> order) {
        if (order == null) {
            return;
        }

        this.order.addAll(order);
    }

    // Alias: ДобавитьПоляУпорядочиванияВОписаниеЗапроса
    public void addOrder(String order) {
        if (order == null) {
            return;
        }

        this.order.add(order);
    }
}
