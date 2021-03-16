package com.revature.orm.db.ddl;

import com.revature.orm.db.Prepared;

// Holds text and info for some kind of table operation
public abstract class PreparedTable extends Prepared {
    protected final String TABLE_NAME;
    public PreparedTable(String name){
        TABLE_NAME = name;
    }
}
