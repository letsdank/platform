package net.letsdank.platform.utils.platform.sql;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class SQLQuery {
    private final Map<String, Object> parameters = new HashMap<>();
    private final JdbcTemplate template = new JdbcTemplate();
    private String sql;

    public SQLQuery() {

    }

    public SQLQuery(String query) {
        this.sql = query;
    }

    public void addParameter(String name, Object value) {
        parameters.put(name, value);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> execute() {
        // TODO: implement
        return Map.of();
    }
}
