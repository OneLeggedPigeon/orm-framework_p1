import com.revature.orm.ORMLogger;
import com.revature.orm.db.StatementManager;
import com.revature.orm.db.connection.ConnectionFactory;
import com.revature.orm.db.ddl.PreparedTable;
import com.revature.orm.db.ddl.create.PreparedTableCreate;
import com.revature.orm.db.dml.create.PreparedInsert;
import com.revature.orm.datatype.DataType;

import java.time.LocalDateTime;

public class TestDriver {

    public static void main(String[] args) {
        ORMLogger.ormLog.debug(LocalDateTime.now()+"Test Log:");

        // test connection
        ConnectionFactory.getInstance();

        // TODO: add dataType enums as args for preparedInsert
        PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        PreparedTable pt = new PreparedTableCreate("test", new String[] {"a","b","c"}, new DataType[] {DataType.BOOL,DataType.TEXT,DataType.FLOAT});
        System.out.println(pt);

        StatementManager.runStatement(pt);
    }
}