package com.orm.update;

public class PreparedInsert extends PreparedStatement {

    // INSERT INTO destination (columns) VALUES (?,...,?)
    public PreparedInsert(String destination, String[] columns) {
        StringBuilder t = new StringBuilder();
        t.append("INSERT INTO ").append(destination).append(" (");
        size = columns.length;
        for (int i = 0; i < size - 1; i++) {
            t.append(columns[i]).append(", ");
        }
        t.append(columns[size - 1]).append(") VALUES(");
        for (int i = 0; i < size - 1; i++) {
            t.append("?, ");
        }
        t.append("?)");
        text = t.toString();
    }
}