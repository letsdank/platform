package net.letsdank.platform.utils.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Таблица значений. Использовать на свой страх и риск.
@Getter
public class TableMap {
    private final List<Column> columns;
    private final List<Row> rows;

    public TableMap() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public void addColumn(String name, Class<?> type) {
        columns.add(new Column(name, type));
    }

    public void addRow() {
        rows.add(new Row(columns.size()));
    }

    public void loadColumn(List<?> values, String columnName) {
        int columnIndex = getColumnIndex(columnName);
        for (int i = 0; i < values.size(); i++) {
            rows.get(i).setCell(columnIndex, values.get(i));
        }
    }

    public void fillValues(Object value, String columnName) {
        int columnIndex = getColumnIndex(columnName);
        for (Row row : rows) {
            row.setCell(columnIndex, value);
        }
    }

    private int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).name().equals(columnName)) {
                return i;
            }
        }

        throw new RuntimeException("Column " + columnName + " not found");
    }

    public int size() {
        return rows.size();
    }

    public TableMap copy(Map<String, Object> rowFilter, String columns) {
        TableMap tableMap = new TableMap();
        List<String> newColumns = List.of(columns.split(","));

        for (Column column : this.columns) {
            if (newColumns.contains(column.name())) {
                tableMap.addColumn(column.name(), column.type());
            }
        }

        for (Row row : rows) {
            for (int i = 0; i < row.cells.length; i++) {
                if (!rowFilter.containsKey(this.columns.get(i).name()) ||
                        rowFilter.get(this.columns.get(i).name()).equals(row.getCell(i))) {
                    tableMap.rows.get(tableMap.rows.size() - 1).setCell(i, row.getCell(i));
                }
            }
        }

        return tableMap;
    }

    public Row getRow(int index) {
        return rows.get(index);
    }

    public Row getRow(String name) {
        return rows.stream().filter(row -> row.getCell(0).equals(name)).findFirst().orElse(null);
    }

    public List<Object> getValues(String columnName) {
        List<Object> values = new ArrayList<>();
        for (Row row : rows) {
            values.add(row.getCell(getColumnIndex(columnName)));
        }
        return values;
    }

    public List<Map<String, Object>> toTableMap() {
        // TODO: Implement
        return null;
    }

    public record Column(String name, Class<?> type) {
    }

    public static class Row {
        private final Object[] cells;

        public Row(int columnCount) {
            cells = new Object[columnCount];
        }

        public void setCell(int index, Object value) {
            cells[index] = value;
        }

        public Object getCell(int index) {
            return cells[index];
        }
    }
}
