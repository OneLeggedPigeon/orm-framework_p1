package com.revature.orm.db.ddl;

import com.revature.orm.config.Config;
import com.revature.orm.config.DBProperties;
import com.revature.orm.read.SQLQueryService;

import java.io.IOException;
import java.util.function.Supplier;

public abstract class TableManager {

    // Check the following:
    // SELECT table_name FROM INFORMATION_SCHEMA.tables where table_schema like 'p_0';
    public static boolean tableExists(String table){
        // TODO
        String schema = "";
        try {
             schema = DBProperties.getInstance().getPropertyByKey("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLQueryService.query("SELECT table_name FROM INFORMATION_SCHEMA.tables WHERE table_schema LIKE '"
                +schema
                +"'");
        return false;
    }
}
