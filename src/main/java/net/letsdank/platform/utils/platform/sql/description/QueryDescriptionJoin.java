package net.letsdank.platform.utils.platform.sql.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QueryDescriptionJoin {
    private String joiningTable;
    private String leadingTable;
    private int joinOrder;
    private String joinType;
    private final List<String> conditions = new ArrayList<>();
}
