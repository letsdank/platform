package net.letsdank.platform.utils.platform.sql;

import net.letsdank.platform.utils.data.TableMap;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class SQLQuery {
    private final Map<String, Object> parameters = new HashMap<>();
    private final JdbcTemplate template = new JdbcTemplate();
    private String sql;
    private TempTableManager tempTableManager;

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

    public TableMap execute() {
        // TODO: implement
        return new TableMap();
    }

    public void setTempTableManager(TempTableManager ttManager) {
        this.tempTableManager = ttManager;
    }
}
