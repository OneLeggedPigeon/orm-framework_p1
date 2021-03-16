package com.orm.read;

import com.orm.connection.ConnectionSession;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class SQLQueryService {
    public static ResultSet query(String statement){
        try (ConnectionSession ses = new ConnectionSession()) {
            Connection conn = ses.getActiveConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
