package com.revature.orm.db.dml.create;

import com.revature.orm.db.Prepared;

public class PreparedInsert extends Prepared {

    // INSERT INTO table (columns) VALUES (?,...,?)
    public PreparedInsert(String table, String[] columns) {
        StringBuilder t = new StringBuilder();
        t.append("INSERT INTO ").append(table).append(" (");
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

    @Override
    public boolean canRun(){
        // TODO
        return true;
    }

}