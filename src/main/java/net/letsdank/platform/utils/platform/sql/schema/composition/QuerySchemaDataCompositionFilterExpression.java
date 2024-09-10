package net.letsdank.platform.utils.platform.sql.schema.composition;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaExpression;

@Getter
@Setter
public class QuerySchemaDataCompositionFilterExpression {
    private QuerySchemaExpression expression;
    private boolean useAttributes;
    private String alias;
}
