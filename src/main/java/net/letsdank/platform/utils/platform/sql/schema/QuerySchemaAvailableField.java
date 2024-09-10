package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaAvailableField {
    private String name;
    private final List<Either<QuerySchemaAvailableField, QuerySchemaAvailableNestedTable>> fields = new ArrayList<>();
    private QuerySchemaFieldRole role;
    private Class<?> valueType;
}
