package com.orm.update;

public class PreparedSelect extends PreparedStatement {

    // SELECT (columns) FROM destination
    public PreparedSelect(String destination, String[] columns) {
        StringBuilder t = new StringBuilder();
        t.append("SELECT ").append(destination).append(" (");
        size = columns.length;
        for (int i = 0; i < size - 1; i++) {
            t.append(columns[i]).append(", ");
        }
        t.append(columns[size - 1]).append(") ");
        t.append("FROM ").append(destination);
        text = t.toString();
    }
}
