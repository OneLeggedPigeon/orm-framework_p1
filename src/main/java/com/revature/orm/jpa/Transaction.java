package com.revature.orm.jpa;

import com.revature.orm.db.Prepared;
import com.revature.orm.db.StatementManager;
import com.revature.orm.db.connection.ConnectionSession;

import javax.persistence.EntityTransaction;
import java.sql.Connection;
import java.util.LinkedList;

public class Transaction implements EntityTransaction {
    private boolean active;
    private final LinkedList<Prepared> statements = new LinkedList<>();

    Transaction(){
        active = false;
    }

    public void add(Prepared sql){
        statements.add(sql);
    }

    @Override
    public void begin() {
        active = true;
        //TODO
    }

    /**
     * Write to the Database
     */
    @Override
    public void commit() {
        if(isActive()){
            ConnectionSession ses = new ConnectionSession();
            Connection conn = ses.getActiveConnection();
            for(Prepared sql : statements){
                StatementManager.runStatement(sql);
            }
            ses.close();
            active = false;
            statements.clear();
        } else {
            throw new IllegalStateException("Transaction wasn't active");
        }
    }

    @Override
    public void rollback() {

    }

    @Override
    public void setRollbackOnly() {

    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
