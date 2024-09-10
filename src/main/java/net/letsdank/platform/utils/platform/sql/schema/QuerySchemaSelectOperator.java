package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.*;
import net.letsdank.platform.utils.platform.sql.schema.composition.QuerySchemaDataCompositionFilterExpression;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaSelectOperator {
    private final List<Either<QuerySchemaExpression, QuerySchemaNestedTable>> fields = new ArrayList<>();
    private boolean selectForUpdate;
    private boolean selectDistinct;
    private final List<QuerySchemaDataCompositionFilterExpression> dataCompositionFilterExpressions = new ArrayList<>();
    private final List<QuerySchemaExpression> group = new ArrayList<>();
    private final List<QuerySchemaSource> sources = new ArrayList<>();
    private Integer retrievedRecordsCount;
    private final List<QuerySchemaExpression> filter = new ArrayList<>();
    private final List<QuerySchemaTableForUpdate> tablesForUpdate = new ArrayList<>();
    private QuerySchemaUnionType unionType;
}
