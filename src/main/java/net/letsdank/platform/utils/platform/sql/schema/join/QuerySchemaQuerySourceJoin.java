package net.letsdank.platform.utils.platform.sql.schema.join;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaExpression;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaSource;

@Getter
@Setter
public class QuerySchemaQuerySourceJoin {
    private QuerySchemaSource source;
    private boolean optionalJoinsGroupBegin;
    private boolean requiredJoin;
    private QuerySchemaJoinType joinType;
    private QuerySchemaExpression condition;
}
