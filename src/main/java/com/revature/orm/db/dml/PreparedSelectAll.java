package com.revature.orm.db.dml;

import com.revature.orm.db.Prepared;
import com.revature.orm.jpa.EntityTemplate;

public class PreparedSelectAll extends Prepared {

    public PreparedSelectAll(EntityTemplate template) {
        super(template);
        isQuery = true;

        text = "SELECT * FROM "+TABLE_SCHEMA+"."+TABLE_NAME;
    }
}
