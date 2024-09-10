package net.letsdank.platform.utils.platform.sql.schema.order;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaColumn;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaExpression;

@Getter
@Setter
public class QuerySchemaOrderExpression {
    private QuerySchemaOrderDirection direction;
    private Either<QuerySchemaExpression, QuerySchemaColumn> item;
}
