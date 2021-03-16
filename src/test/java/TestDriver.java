import com.orm.ORMLogger;
import com.orm.dml.create.PreparedInsert;
import com.orm.ddl.PreparedTable;
import com.orm.datatype.DataType;
import com.orm.dml.StatementManager;

public class TestDriver {

    public static void main(String[] args) {
        ORMLogger.ormLog.debug("Test Log:");

        // TODO: add dataType enums as args for preparedInsert
        PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        PreparedTable pt = new PreparedTable("test", new String[] {"a","b","c"}, new DataType[] {DataType.BOOL,DataType.TEXT,DataType.FLOAT});
        System.out.println(pt);

        StatementManager.runStatement(pt);
    }
}