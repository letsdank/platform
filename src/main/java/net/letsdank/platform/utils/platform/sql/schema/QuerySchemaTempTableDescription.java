package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaTempTableDescription {
    private final List<Either<QuerySchemaAvailableField, QuerySchemaAvailableNestedTable>> availableFields = new ArrayList<>();
    private String tableName;
    private String alias;
}
