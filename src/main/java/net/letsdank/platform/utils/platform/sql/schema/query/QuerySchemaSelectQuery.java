package net.letsdank.platform.utils.platform.sql.schema.query;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaColumn;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaNestedTableColumn;
import net.letsdank.platform.utils.platform.sql.schema.QuerySchemaSelectOperator;
import net.letsdank.platform.utils.platform.sql.schema.order.QuerySchemaOrderExpression;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaSelectQuery {
    private boolean selectAllowed;
    private String placementTable;

    // TODO: implement others
    private final List<QuerySchemaOrderExpression> order = new ArrayList<>();
    private final List<QuerySchemaSelectOperator> operators = new ArrayList<>();
    private final List<Either<QuerySchemaColumn, QuerySchemaNestedTableColumn>> columns = new ArrayList<>();
}
