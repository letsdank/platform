package net.letsdank.platform.utils.platform.sql.description;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryDescriptionTable {
    private String name;
    private String alias;
    private String type;
    private QueryDescription queryDescription;
}
