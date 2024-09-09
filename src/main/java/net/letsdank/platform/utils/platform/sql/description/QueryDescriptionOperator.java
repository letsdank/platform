package net.letsdank.platform.utils.platform.sql.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QueryDescriptionOperator {
    private String joinType;
    private boolean distinct;
    private Integer recordCount;
    private final List<String> selectedFields = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();
    private final List<QueryDescriptionReportField> selectedReportFields = new ArrayList<>();
    private final List<QueryDescriptionTable> tables = new ArrayList<>();
    private final List<QueryDescriptionJoin> joins = new ArrayList<>();
    private final List<String> conditions = new ArrayList<>();
    private final List<String> groups = new ArrayList<>();
    private final List<String> having = new ArrayList<>();
    private final Map<String, QueryDescriptionTable> sources = new HashMap<>();
    private final List<String> reportFieldConditions = new ArrayList<>();
}
