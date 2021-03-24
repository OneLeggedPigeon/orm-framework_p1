package com.revature.orm.db.ddl;

import com.revature.orm.db.Prepared;
import com.revature.orm.jpa.EntityTemplate;

public class PreparedTableSelect extends Prepared {

    public PreparedTableSelect(EntityTemplate template) {
        super(template);
        text = "SELECT * FROM INFORMATION_SCHEMA.tables WHERE table_schema LIKE '"+TABLE_SCHEMA+"'";
    }

    public PreparedTableSelect(String schema) {
        text = "SELECT * FROM INFORMATION_SCHEMA.tables WHERE table_schema LIKE '"+schema+"'";
    }
}