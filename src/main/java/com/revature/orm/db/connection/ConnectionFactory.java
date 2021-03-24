package com.revature.orm.db.connection;

import com.revature.orm.OrmLogger;
import com.revature.orm.config.DatabaseProperties;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory implements Closeable {

    public static final int MAX_CONNECTIONS = 4;
    private final Connection[] connectionPool = new Connection[MAX_CONNECTIONS];

    private static ConnectionFactory instance;

    private ConnectionFactory() {
        for(int i = 0; i< MAX_CONNECTIONS; i++){

            //OrmLogger.ormLog.debug("adding connection number "+i+" to the connection pool");
            connectionPool[i] = createConnection();
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
    private Connection createConnection() {

        try {
            DatabaseProperties props = DatabaseProperties.getInstance();

            String connectionTemplate = props.getProfile();
            //TODO check if I can remove the schema specification here
            String url = props.getPropertyByKey(connectionTemplate+".url")+"?currentSchema="+props.getPropertyByKey(connectionTemplate+".schema");
            return DriverManager.getConnection(
                    url,
                    props.getPropertyByKey(connectionTemplate+".username"),
                    props.getPropertyByKey(connectionTemplate+".password"));
        } catch (SQLException e) {
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
