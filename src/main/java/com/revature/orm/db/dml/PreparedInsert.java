package com.revature.orm.db.dml;

import com.revature.orm.db.Prepared;
import com.revature.orm.db.StatementService;
import com.revature.orm.jpa.EntityTemplate;

import java.sql.Connection;

public class PreparedInsert extends Prepared {

    /**
     * <pre>
     *     INSERT INTO schema.table (columns) VALUES (?,...,?) RETURNING idColumn
     * </pre>
     * Inserts a new value and returns the new serial idColumn
     * @param template EntityTemplate
     */
    public PreparedInsert(EntityTemplate template) {
        super(template);
        isQuery = true;

        StringBuilder t = new StringBuilder();
        t.append("INSERT INTO ").append(TABLE_SCHEMA).append(".").append(TABLE_NAME).append(" (");
        for (int i = 0; i < SIZE; i++) {
            t.append(COLUMNS.get(i).getName());
            if (i < SIZE - 1) t.append(", ");
        }
        t.append(") VALUES (");
        for (int i = 0; i < SIZE; i++) {
            t.append("?");
            if(i < SIZE - 1) t.append(", ");
        }
        t.append(") RETURNING ").append(template.getIdColumn().getName());
        text = t.toString();
    }
}