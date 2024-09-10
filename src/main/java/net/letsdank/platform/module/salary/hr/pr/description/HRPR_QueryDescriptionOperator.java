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
}
