package com.orm.update;

public class PreparedTable extends PreparedStatement{

    /*
     * arguments:
     *  name: the name of the new table
     *  columns[][0]: array of columns to add
     *  columns[][1]: array of matched data types for each column
     *
     * table creation for postgres TODO: change the pk setting based on driver type
     *  CREATE TABLE name (
     *      name+'_id' serial NOT NULL,
     *      columns[0][0] columns[0][1],
     *      columns[1][0] columns[1][1],
     *      ...,
     *      columns[][0] columns[][1],
     *      PRIMARY KEY (name+'_id')
     *  );
     */
    public PreparedTable(String name, String[][] columns) {
        StringBuilder t = new StringBuilder();
        String pk = name+"_id";
        t.append("CREATE TABLE").append(name).append(" (");
        t.append(pk).append(" serial NOT NULL, ");
        size = columns.length;
        for (int i = 0; i < size; i++) {
            t.append(columns[i][0]).append(" ").append(columns[i][1]).append(", ");
        }
        t.append("PRIMARY KEY (").append(pk).append("))");
        text = t.toString();
    }
}
