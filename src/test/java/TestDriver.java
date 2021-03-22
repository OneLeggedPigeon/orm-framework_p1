import com.revature.orm.OrmLogger;
import com.revature.orm.db.connection.ConnectionFactory;

import java.time.LocalDateTime;

public class TestDriver {

    public static void main(String[] args) {
        OrmLogger.ormLog.debug(LocalDateTime.now()+"Test Log:");

        // test connection
        ConnectionFactory.getInstance();

        // TODO: add dataType enums as args for preparedInsert
        /*PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        PreparedTable pt = new PreparedTableCreate("test", new String[] {"a","b","c"}, new DataType[] {DataType.BOOL,DataType.TEXT,DataType.FLOAT});
        System.out.println(pt);

        //TODO: Debug pt.canRun()
        System.out.println(pt.canRun());
        StatementManager.runStatement(pt);*/
    }
}