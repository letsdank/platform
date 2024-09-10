package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaColumn {
    private final List<Either<QuerySchemaExpression, QuerySchemaNestedTable>> fields = new ArrayList<>();
    private String alias;
    private Class<?> valueType;
}
