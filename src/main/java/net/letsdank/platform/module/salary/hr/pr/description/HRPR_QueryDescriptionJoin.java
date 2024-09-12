package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HRPR_QueryDescriptionJoin {
    private String leadingTable;
    private String joiningTable;
    private int joinOrder;
    private List<String> conditions = new ArrayList<>();
    private String joinType; // TODO: Can be enum

    private String templateJoinCondition;
}