import com.orm.ORMLogger;
import com.orm.connection.ConnectionSession;
import com.orm.update.PreparedInsert;
import com.orm.update.PreparedTable;

public class TestDriver {

    public static void main(String[] args) {
        ORMLogger.ormLog.debug("Test Log:");

        PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        PreparedTable pt = new PreparedTable("test", new String[][] {{"a","int"},{"b","varchar(255)"},{"b","bool"}});
        System.out.println(pt);
    }
}