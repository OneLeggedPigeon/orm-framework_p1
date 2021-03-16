package com.orm.ddl;

import com.orm.Prepared;
import com.orm.datatype.DataTypeEnums;
import com.orm.datatype.DataType;

public class PreparedTable extends Prepared {

    /*
     * arguments:
     *  name: the name of the new table
     *  columns[]: array of columns to add
     *  dataTypes[]: array of matched DataType enums for each column
     *
     * table creation
     *  CREATE TABLE name (
     *      name+'_id' serial NOT NULL,
     *      columns[0][0] columns[0][1],
     *      columns[1][0] columns[1][1],
     *      ...,
     *      columns[][0] columns[][1],
     *      PRIMARY KEY (name+'_id')
     *  );
     */
    public PreparedTable(String name, String[] columns, DataType[] dataTypes) {
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
}
