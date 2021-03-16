package com.revature.orm.db.ddl.create;

import com.revature.orm.datatype.DataType;
import com.revature.orm.datatype.DataTypeEnums;
import com.revature.orm.db.ddl.PreparedTable;
import com.revature.orm.db.ddl.TableManager;

// Holds information for a CREATE table operation
public class PreparedTableCreate extends PreparedTable {

    /**
     * table creation
     *      CREATE TABLE name (
     *      name+'_id' serial NOT NULL,
     *      columns[0][0] columns[0][1],
     *      columns[1][0] columns[1][1],
     *      ...,
     *      columns[][0] columns[][1],
     *      PRIMARY KEY (name+'_id')
     *  );
     * @param name the name of the new table
     * @param columns array of columns to add
     * @param dataTypes array of matched DataType enums for each column
     */
    public PreparedTableCreate(String name, String[] columns, DataType[] dataTypes) {
        super(name);
        StringBuilder t = new StringBuilder();
        DataTypeEnums data = DataTypeEnums.getInstance();
        String pk = name+"_id";

        t.append("CREATE TABLE ").append(name).append(" (");
        t.append(pk).append(" ").append(data.getDataType(DataType.SERIAL)).append(" NOT NULL, ");
        size = columns.length;
        for (int i = 0; i < size; i++) {
            t.append(columns[i]).append(" ").append(data.getDataType(dataTypes[i])).append(", ");
        }
        t.append("PRIMARY KEY (").append(pk).append("))");
        text = t.toString();
    }

    /**
     * @return True if the it would be wise to run the constructor here.
     */
    @Override
    public boolean canRun() {
        return !TableManager.tableExists(TABLE_NAME);
    }
}
