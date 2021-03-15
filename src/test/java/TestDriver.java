import com.orm.connection.ConnectionSession;
import com.orm.update.PreparedInsert;
import org.apache.log4j.Logger;

public class TestDriver {
    static Logger log = Logger.getLogger(TestDriver.class.getName());

    public static void main(String[] args) {
        PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        ConnectionSession c = new ConnectionSession();
        c.getActiveConnection();

        log.debug("logging");
    }
}
