package com.revature.orm.db.dml.read;

import com.revature.orm.db.Prepared;

public class PreparedSelect extends Prepared {

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

    @Override
    public boolean canRun(){
        // TODO
        return true;
    }
}
