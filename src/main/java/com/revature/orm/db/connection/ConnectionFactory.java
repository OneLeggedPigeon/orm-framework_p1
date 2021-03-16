package com.revature.orm.db.connection;

import com.revature.orm.ORMLogger;
import com.revature.orm.config.Config;
import com.revature.orm.config.DBProperties;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory implements Closeable {

    public static final int MAX_CONNECTIONS = 4;
    private final Connection[] connectionPool = new Connection[MAX_CONNECTIONS];

    private static ConnectionFactory instance;

    private ConnectionFactory() {
        for(int i = 0; i< MAX_CONNECTIONS; i++){

            ORMLogger.ormLog.debug("adding connection number "+i+" to the connection pool");

            try {
                connectionPool[i] = createConnection(Config.getInstance().getPropertyByKey("connection-profile"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }


    @SuppressWarnings("SameParameterValue")
    private Connection createConnection(String profile) {

        try {
            DBProperties props = DBProperties.getInstance();

            String connectionTemplate = props.getProfile();
            String url = props.getPropertyByKey(connectionTemplate+".url")+"?currentSchema="+props.getPropertyByKey(connectionTemplate+".schema");
            return DriverManager.getConnection(
                    url,
                    props.getPropertyByKey(connectionTemplate+".username"),
                    props.getPropertyByKey(connectionTemplate+".password"));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection[] getConnectionPool() {
        return connectionPool;
    }

    @Override
    public void close(){
        for(Connection con: connectionPool){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
