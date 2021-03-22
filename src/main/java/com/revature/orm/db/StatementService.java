package com.revature.orm.db;

import com.revature.orm.db.dml.SQLQueryService;
import com.revature.orm.jpa.EntityTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A collection of static methods for generating and running Prepared Statement SQL Strings from <code>Prepared</code> Objects
 */
public abstract class StatementService {

    public static boolean intMatch(Connection conn, String table, String schema, String columnLabel, int value){
        try {
            ResultSet rs = SQLQueryService.query(conn, "SELECT "+columnLabel+" FROM "+schema+"."+table+" WHERE "+columnLabel+" = "+value);
            // false if there were no matches
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean idExists(Connection conn, String table, String schema, String idColumn, int id) {
        return intMatch(conn,table,schema,idColumn,id);
    }

    /**
     *
     * @param template EntityTemplate containing the info and methods necessary to determine column parameters, extract new column values, and set object values from read columns
     * @return An object containing multiple SQL statements, usage to be determined at connection time
     */
    public static VariablePrepared generateSql(EntityTemplate template) {
        return new VariablePrepared(template);
    }
}