package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FilterUsageDescription {
    private String filterTableAlias;
    private String registryTableAlias;
    private String parameterNamePostfix;
    private String templateJoinCondition = "";
    private List<String> joinConditions = new ArrayList<>();
    private Map<String, String> filterDimensions; // TODO правильно?
    private Map<String, String> filterPeriodFields; // TODO
    private Map<String, String> additionalFields; // TODO
    private Map<String, String> joinDimensions; // TODO правильно?
    private HRPR_QueryDescriptionOperator queryOperator;
    private boolean filterAsTT = false;
    private boolean allRecords = false;
    private List<String> calculatedPeriodParametersDescription = new ArrayList<>();
}
