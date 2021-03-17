package com.revature.orm.jpa;

import javax.persistence.EntityTransaction;

public class RevatureEntityTransaction implements EntityTransaction {
    //TODO
    @Override
    public void begin() {

    }

    //TODO
    @Override
    public void commit() {

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
        return false;
    }
}
