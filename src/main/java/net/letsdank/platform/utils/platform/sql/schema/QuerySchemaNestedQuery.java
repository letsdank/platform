package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.query.QuerySchemaSelectQuery;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaNestedQuery {
    private final List<Either<QuerySchemaAvailableField, QuerySchemaAvailableNestedTable>> availableFields = new ArrayList<>();
    private QuerySchemaSelectQuery query;
    private String alias;
}
