package com.revature.orm.db.dml;

import com.revature.orm.db.Prepared;
import com.revature.orm.jpa.EntityTemplate;

//TODO
public class PreparedDelete extends Prepared {
    /**
     * <pre>
     *     DELETE FROM schema.table WHERE id = ?
     * </pre>
     */
    public PreparedDelete(EntityTemplate template) {
        super(template);
        idParameterLast = true;
        hasIdParameter = true;
        text = "DELETE FROM "+TABLE_SCHEMA+"."+TABLE_NAME+" WHERE "+template.getIdColumn().getName()+" = ?";
    }
}
