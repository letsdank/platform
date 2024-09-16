package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.sql.schema.join.QuerySchemaJoinType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HRPR_QueryDescriptionJoin {
    private String leadingTable;
    private String joiningTable;
    private int joinOrder;
    private List<String> conditions = new ArrayList<>();
    private QuerySchemaJoinType joinType;

    private String templateJoinCondition;
}
