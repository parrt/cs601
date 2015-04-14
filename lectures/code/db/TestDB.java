import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) throws Exception {
        String dbFile = "/tmp/test.db";
        Connection db = null;
        long start = System.currentTimeMillis();
        try {
            Class.forName("org.sqlite.JDBC"); // force load of driver
            db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            // DO SOMETHINE WITH db to read/write
//            Statement statement = db.createStatement();
//            statement.executeUpdate("create table person (id integer, name string)");
//            statement.executeUpdate("insert into person values(1, 'leo')");
//            statement.close();

            long stop = System.currentTimeMillis();
            System.out.printf("SQL exe time %1.1f seconds\n", (stop-start)/1000.0);
        }
        finally {
            if ( db!=null ) {
                db.close();
            }
        }
    }
}
