package com.revature.orm.db;

import com.revature.orm.db.ddl.PreparedTableCreate;
import com.revature.orm.db.dml.*;
import com.revature.orm.jpa.ContextType;
import com.revature.orm.jpa.EntityTemplate;

import java.util.ArrayList;

/**
 * Holds one or more Prepared Objects, which ones to be used is determined at connection time.
 */
public class VariablePrepared {
    private final ArrayList<Prepared> persistStatements = new ArrayList<>();
    private final ArrayList<Prepared> removeStatements = new ArrayList<>();
    private final ArrayList<Prepared> readStatements = new ArrayList<>();

    public VariablePrepared(EntityTemplate template){
        generateSql(template);
    }

    private void generateSql(EntityTemplate template) {
        // PERSIST
        // generate CREATE TABLE
        persistStatements.add(new PreparedTableCreate(template));
        // generate ALTER TABLE
//        persistStatements.add(new PreparedTableAlter(template));
        // generate UPDATE
        persistStatements.add(new PreparedUpdate(template));
        // generate INSERT
        persistStatements.add(new PreparedInsert(template));
        // REMOVE
        // generate DELETE
        removeStatements.add(new PreparedDelete(template));
        // READ
        // generate SELECT ROWS BY Id
        readStatements.add(new PreparedSelectById(template));
        // generate SELECT ROWS
//        readStatements.add(new PreparedSelectAll(template));
    }

    public boolean canRun() {
        return false;
    }

    public ArrayList<Prepared> getStatements(ContextType type) {
        switch(type){
            case PERSIST:
                return persistStatements;
            case REMOVE:
                return removeStatements;
            case READ:
                return readStatements;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public ArrayList<Prepared> getReadStatements(ContextType type) {
        return readStatements;
    }
}
