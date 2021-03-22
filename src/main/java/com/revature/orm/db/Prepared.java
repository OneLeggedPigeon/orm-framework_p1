package com.revature.orm.db;

import com.revature.orm.jpa.EntityTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

// parent of classes that hold data on Prepared Statements
public abstract class Prepared {
    protected String text;

    // set to true if there are '?' in text
    protected boolean hasIdParameter = false;
    protected boolean idParameterLast = false;
    // does text return a value?
    protected boolean isQuery = false;

    protected final String TABLE_NAME;
    protected final String TABLE_SCHEMA;
    protected final ArrayList<EntityTemplate.Col> COLUMNS;
    protected final int SIZE;

    public Prepared(){
        TABLE_NAME = null;
        TABLE_SCHEMA = null;
        COLUMNS = null;
        SIZE = 0;
    }
    public Prepared(EntityTemplate template){
        TABLE_NAME = template.getTable();
        TABLE_SCHEMA = template.getSchema();
        COLUMNS = template.getColumns();
        SIZE = COLUMNS.size();
    }

    public int size(){
        return SIZE;
    }

    public String toString() {
        return text;
    }

    public PreparedStatement getPreparedStatement(Connection conn){
        try {
            return conn.prepareStatement(text);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Statement could not be prepared.");
        }
    }

    public boolean isIdParameterLast() {
        return idParameterLast;
    }

    public boolean hasIdParameter() {
        return hasIdParameter;
    }

    public boolean isQuery() {
        return isQuery;}
}