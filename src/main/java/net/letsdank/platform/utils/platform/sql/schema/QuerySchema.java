package net.letsdank.platform.utils.platform.sql.schema;

import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.schema.query.QuerySchemaSelectQuery;
import net.letsdank.platform.utils.platform.sql.schema.query.QuerySchemaTableDropQuery;

import java.util.ArrayList;
import java.util.List;

public class QuerySchema {
    private String query;
    private final List<Either<QuerySchemaSelectQuery, QuerySchemaTableDropQuery>> packets = new ArrayList<>();

    public QuerySchema() {
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Either<QuerySchemaSelectQuery, QuerySchemaTableDropQuery>> getPackets() {
        return packets;
    }
}
