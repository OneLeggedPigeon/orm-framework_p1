package com.revature.orm.db.ddl;

import com.revature.orm.config.DBProperties;
import com.revature.orm.db.dml.read.SQLQueryService;

import java.sql.SQLException;

public abstract class TableManager {

    // Check the following:
    // SELECT table_name FROM INFORMATION_SCHEMA.tables where table_schema like 'p_0';
    public static boolean tableExists(String table){
        // TODO
        String schema = DBProperties.getInstance().getPropertyByKey("");
        try {
            SQLQueryService.query("SELECT table_name FROM INFORMATION_SCHEMA.tables WHERE table_schema LIKE '"
                    +schema
                    +"'")
            .getString(table);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
