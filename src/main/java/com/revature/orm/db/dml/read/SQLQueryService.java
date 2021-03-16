package com.revature.orm.db.dml.read;

import com.revature.orm.ORMLogger;
import com.revature.orm.db.connection.ConnectionSession;

import java.sql.*;

public abstract class SQLQueryService {

    /**
     *
     * @param statement A SQL <code>String</code> to be turned into a <code>Statement</code>
     *                  with a new <code>Connection</code> from <code>ConnectionSession</code>
     * @return a <code>ResultSet</code> object that contains the data produced
     *         by the given query; never <code>null</code>
     * @exception SQLException if a database access error occurs,
     * this method is called on a closed <code>Statement</code>, the given
     *            SQL statement produces anything other than a single
     *            <code>ResultSet</code> object, the method is called on a
     * <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     * timeout value that was specified by the {@code setQueryTimeout}
     * method has been exceeded and has at least attempted to cancel
     * the currently running {@code Statement}
     */
    public static ResultSet query(String statement) throws SQLException {
        try (ConnectionSession ses = new ConnectionSession()) {
            Connection conn = ses.getActiveConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(statement);
        } catch (SQLException e) {
            ORMLogger.ormLog.debug("Failed Query: "+statement);
            throw e;
        }
    }
}
