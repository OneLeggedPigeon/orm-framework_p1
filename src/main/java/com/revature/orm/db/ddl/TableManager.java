package com.revature.orm.db.ddl;

import com.revature.orm.config.DBProperties;
import com.revature.orm.db.dml.read.SQLQueryService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public abstract class TableManager {

    // A hashset of the names of all the tables in the current schema, per last read
    private static final HashSet<String> tableNames = new HashSet<>();

    private static boolean tablesChanged = true;

    // Check the following:
    // SELECT table_name FROM INFORMATION_SCHEMA.tables where table_schema like 'p_0';
    public static boolean tableExists(String table){
        return getTableNames().contains(table);
    }

    // Reads the name of every table into HashSet<String> tableNames
    private static void readTableNames(){
        DBProperties prop = DBProperties.getInstance();
        String schema = prop.getSchema();
        try {
            ResultSet rs = SQLQueryService.query("SELECT * FROM INFORMATION_SCHEMA.tables WHERE table_schema LIKE '"+schema+"'");
            tableNames.clear();
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
            tablesChanged = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // returns the table names, only reading from the database if there has been an update
    public static HashSet<String> getTableNames() {
        if(tablesChanged){
            readTableNames();
        }
        return tableNames;
    }

    // Call this whenever tables are changed or removed
    public static void setTablesChanged(boolean tablesChanged) {
        TableManager.tablesChanged = tablesChanged;
    }
}
