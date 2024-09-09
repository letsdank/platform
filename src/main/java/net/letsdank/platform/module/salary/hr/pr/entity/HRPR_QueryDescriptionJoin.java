package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HRPR_QueryDescriptionJoin {
    private String joiningTable;
    private String leadingTable;
    private int joinOrder;
    private String joinType;
    private final List<String> conditions = new ArrayList<>();
    private String templateJoinCondition;
}
