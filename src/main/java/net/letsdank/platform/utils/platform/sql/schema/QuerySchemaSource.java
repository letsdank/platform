package net.letsdank.platform.utils.platform.sql.schema;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either3;
import net.letsdank.platform.utils.platform.sql.schema.join.QuerySchemaQuerySourceJoin;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuerySchemaSource {
    private Either3<QuerySchemaTable, QuerySchemaNestedQuery, QuerySchemaTempTableDescription> source;
    private final List<QuerySchemaQuerySourceJoin> joins = new ArrayList<>();

    public String getSourceAlias() {
        if (source.isLeft()) return source.left().getAlias();
        else if (source.isMiddle()) return source.middle().getAlias();
        return source.right().getAlias();
    }
}
