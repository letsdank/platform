package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRPR_QueryDescriptionOperator {
    private String joinType;
    private boolean distinct;
    private Integer recordCount;
    private final List<String> selectedFields = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();
    private final List<HRPR_QueryDescriptionReportField> selectedReportFields = new ArrayList<>();
    private final List<HRPR_QueryDescriptionTable> tables = new ArrayList<>();
    private final List<HRPR_QueryDescriptionJoin> joins = new ArrayList<>();
    private final List<String> conditions = new ArrayList<>();
    private final List<String> groups = new ArrayList<>();
    private final List<String> having = new ArrayList<>();
    private final Map<String, HRPR_QueryDescriptionTable> sources = new HashMap<>();
    private final List<String> reportFieldConditions = new ArrayList<>();
}
