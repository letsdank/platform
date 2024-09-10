package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.composition.QuerySchemaDataCompositionFilterExpression;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaTable {
    private final List<Either<QuerySchemaAvailableField, QuerySchemaAvailableNestedTable>> availableFields = new ArrayList<>();
    private String tableName;
    private final List<QuerySchemaTableParameter> parameters = new ArrayList<>();
    private final List<Either<List<QuerySchemaDataCompositionFilterExpression>,
            List<QuerySchemaTableParameter>>> dataCompositionParameters = new ArrayList<>();
    private String alias;
}
