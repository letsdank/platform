package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
}
