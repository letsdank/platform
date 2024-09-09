package net.letsdank.platform.utils.platform.sql.schema;

import java.util.ArrayList;
import java.util.List;

public class QuerySchema {
    private String query;
    private List<QuerySchemaPacket> packets = new ArrayList<>();

    public QuerySchema() {
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<QuerySchemaPacket> getPackets() {
        return packets;
    }
}
