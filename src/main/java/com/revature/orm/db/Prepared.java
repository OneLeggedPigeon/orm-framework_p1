package com.revature.orm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// parent of classes that hold data on Prepared Statements
public abstract class Prepared {
    protected String text;
    // what this means depends on the statement, e.g. for insert its the number of columns to effect
    protected int size;

    public String toString() {
        return text;
    }

    public int size(){
        return size;
    }

    public PreparedStatement getPreparedStatement(Connection conn){
        PreparedStatement result = null;
        try {
            result =conn.prepareStatement(text);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    // Returns whether or not it makes sense to run the statement.
    public abstract boolean canRun();
}