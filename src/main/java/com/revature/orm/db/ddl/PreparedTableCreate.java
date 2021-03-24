package com.revature.orm.db.ddl;

import com.revature.orm.datatype.DataTypeEnums;
import com.revature.orm.db.Prepared;
import com.revature.orm.jpa.EntityTemplate;

import java.util.ArrayList;

// Holds information for a CREATE table operation
public class PreparedTableCreate extends Prepared {

    /**
     * <pre>
     *     CREATE TABLE schema.table (
     *     $PRIMARY_KEY$ serial NOT NULL,
     *     columns.get(0).getName() columns.get(0).getDataType(),
     *     columns.get(1).getName() columns.get(1).getDataType(),
     *     ...,
     *     columns.get(n-1).getName() columns.get(n-1).getDataType(),
     *     PRIMARY KEY ($PRIMARY_KEY$))
     * </pre>
     * @param template Entity Structure read in from Annotations
     */
    public PreparedTableCreate(EntityTemplate template) {
        super(template);
        ArrayList<EntityTemplate.Col> columns = template.getColumns();
        StringBuilder t = new StringBuilder();
        DataTypeEnums data = DataTypeEnums.getInstance();
        String pk = template.getIdColumn().getName();

        t.append("CREATE TABLE ").append(TABLE_SCHEMA).append(".").append(TABLE_NAME).append(" (");
        t.append(pk).append(" SERIAL NOT NULL, ");
        for (int i = 0; i < SIZE; i++) {
            EntityTemplate.Col column = columns.get(i);
            t.append(column.getName()).append(" ");
            t.append(data.getDataTypeString(column.getDataType()));
            t.append(", ");
        }
        t.append("PRIMARY KEY (").append(pk).append("))");
        text = t.toString();
    }
}
