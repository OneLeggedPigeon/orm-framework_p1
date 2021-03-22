package com.revature.orm.db.ddl;

import com.revature.orm.db.dml.SQLQueryService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Collection of Static methods for querying Database Tables
 */
public abstract class TableManager {

    public static boolean tableExists(Connection conn, String table, PreparedTableSelect sql){
        return getTableNames(conn, sql.toString()).contains(table);
    }

    // SELECT table_name FROM INFORMATION_SCHEMA.tables where table_schema like 'p_0';
    public static HashSet<String> getTableNames(Connection conn, String sql) {
        HashSet<String> result = new HashSet<>();
        try {
            ResultSet rs = SQLQueryService.query(conn, sql);
            while (rs.next()) {
                result.add(rs.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
