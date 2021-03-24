package com.revature.orm.db.connection;

import com.revature.orm.OrmLogger;

import java.sql.Connection;

public class ConnectionSession implements AutoCloseable {

    private Connection activeConnection;
    private int locationIndex = -1;

    public Connection getActiveConnection(){
        for(int i = 0; i< ConnectionFactory.MAX_CONNECTIONS; i++){
            Connection conn = ConnectionFactory.getInstance().getConnectionPool()[i];
            if(conn != null){
                //OrmLogger.ormLog.debug("gathering connection id: " + i + " to give to the object");

                activeConnection = conn;
                ConnectionFactory.getInstance().getConnectionPool()[i] = null;
                locationIndex = i;
                return activeConnection;
            }
        }
        throw new RuntimeException("No active connections available");
    }

    @Override
    public void close(){
        //OrmLogger.ormLog.debug("closing the session and giving connection id: " + locationIndex + " back to the connection pool");
        ConnectionFactory.getInstance().getConnectionPool()[locationIndex]=activeConnection;
        activeConnection = null;
        locationIndex = -1;
    }
}
