package com.revature.orm.db.dml;

import com.revature.orm.db.Prepared;
import com.revature.orm.jpa.EntityTemplate;

public class PreparedUpdate extends Prepared {

    /**
     * <pre>
     *     UPDATE schema.table
     *     SET column0 = ?, column1 = ?, ...
     *     WHERE idColumn = ?
     * </pre>
     * @param template EntityTemplate
     */
    public PreparedUpdate(EntityTemplate template) {
        super(template);
        idParameterLast = true;
        hasIdParameter = true;

        StringBuilder t = new StringBuilder();
        t.append("UPDATE ").append(TABLE_SCHEMA).append(".").append(TABLE_NAME);
        t.append(" SET ");
        // add every non-id column
        for (int i = 0; i < SIZE; i++) {
            t.append(COLUMNS.get(i).getName()).append(" = ?");
            if (i < SIZE - 1) t.append(", ");
        }
        t.append(" WHERE ").append(template.getIdColumn().getName()).append(" = ?");
        text = t.toString();
    }
}
