package com.revature.orm.db;

import com.revature.orm.ORMLogger;
import com.revature.orm.db.connection.ConnectionSession;

import java.sql.Connection;
import java.sql.PreparedStatement;

// TODO: get this to run with parameters
public abstract class StatementManager {

    public static void runStatement(Prepared sql) {
        if(sql.canRun()) {
            try (ConnectionSession ses = new ConnectionSession()) {
                Connection conn = ses.getActiveConnection();
                PreparedStatement ps = sql.getPreparedStatement(conn);
                // add to ?
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ORMLogger.ormLog.debug("Did not run: "+sql);
        }
    }
}