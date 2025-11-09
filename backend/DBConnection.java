import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/techhh", "root", ""); // default XAMPP user/pass
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}

