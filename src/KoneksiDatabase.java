import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDatabase {

    private static final String URL  = "jdbc:postgresql://localhost:5432/db_gym";
    private static final String USER = "postgres";
    private static final String PASS = "admin"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
}
