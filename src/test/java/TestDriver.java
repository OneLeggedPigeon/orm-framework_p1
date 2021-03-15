import com.orm.connection.ConnectionSession;
import com.orm.update.PreparedInsert;

import static org.mockito.Mockito.mock;

public class TestDriver {
    public static void main(String[] args) {
        PreparedInsert ps = new PreparedInsert("dest", new String[] {"a","b","c","d"});
        System.out.println(ps);

        ConnectionSession c = new ConnectionSession();
        c.getActiveConnection();
    }
}
