package com.orm.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Prepared {
    protected String text;
    protected int size;

    public String toString() {
        return text;
    }

    public int size(){
        return size;
    }

    public PreparedStatement getStatement(Connection conn){
        java.sql.PreparedStatement result = null;
        try {
            result =conn.prepareStatement(text);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}