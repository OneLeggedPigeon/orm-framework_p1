package com.orm.update;

public abstract class PreparedStatement {
    protected String text;
    protected int size;

    public String toString() {
        return text;
    }

    public int size(){
        return size;
    }
}