package com.revature.connection;

import java.sql.Connection;

public class ConnectionSession implements AutoCloseable {

    private Connection activeConnection;
    private int locationIndex = -1;

    public Connection getActiveConnection(){
        for(int i = 0; i< com.revature.db.ConnectionFactory.MAX_CONNECTIONS; i++){
            Connection conn = com.revature.db.ConnectionFactory.getInstance().getConnectionPool()[i];
            if(conn != null){
                //System.out.println("gathering connection id: " + i + " to give to the object");
                activeConnection = conn;
                com.revature.db.ConnectionFactory.getInstance().getConnectionPool()[i] = null;
                locationIndex = i;
                return activeConnection;
            }
        }
        throw new RuntimeException("No active connections available");
    }

    @Override
    public void close(){
        //System.out.println("closing the session and giving connection id: " + locationIndex + " back to the connection pool");
        com.revature.db.ConnectionFactory.getInstance().getConnectionPool()[locationIndex]=activeConnection;
        activeConnection = null;
        locationIndex = -1;
    }
}
