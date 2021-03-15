package com.orm.connection;

import com.orm.ORMLogger;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.Log4JLogRecord;

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

            Properties props = new Properties();
            try {
                props.load(new FileReader("src/main/resources/orm.config"));
                connectionPool[i] = createConnection(props.getProperty("connection-profile"));
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
        Properties props = new Properties();
        try {
            props.load(new FileReader("src/main/resources/orm.properties"));

            String connectionTemplate = "com.revature.orm." + profile;
            return DriverManager.getConnection(
                    props.getProperty(connectionTemplate+".url"),
                    props.getProperty(connectionTemplate+".username"),
                    props.getProperty(connectionTemplate+".password"));
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
