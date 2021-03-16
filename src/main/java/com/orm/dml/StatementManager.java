package com.orm.dml;

import com.orm.Prepared;
import com.orm.connection.ConnectionSession;

import java.sql.Connection;
import java.sql.PreparedStatement;

// TODO: get this to run with parameters
public abstract class StatementManager {

    public static void runStatement(Prepared sql) {
        try (ConnectionSession ses = new ConnectionSession()) {
            Connection conn = ses.getActiveConnection();
            PreparedStatement ps = sql.getStatement(conn);
            // add to ?
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}