package net.letsdank.platform.utils.data;

import java.util.ArrayList;
import java.util.List;

// Таблица значений. Использовать на свой страх и риск.
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
            if (columns.get(i).getName().equals(columnName)) {
                return i;
            }
        }

        throw new RuntimeException("Column " + columnName + " not found");
    }

    private class Column {
        private final String name;
        private final Class<?> type;

        public Column(String name, Class<?> type) {
            this.type = type;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }
    }

    private class Row {
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
