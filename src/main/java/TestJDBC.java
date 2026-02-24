/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// mvn exec:java '-Dexec.mainClass=TestJDBC'
public class TestJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://127.0.0.1:3307/gestion_aulas?user=guti&password=guti";
        String query = "SELECT 1+1 AS result";

        try {
            // Load the MariaDB JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");

            // Establish connection
            Connection conn = DriverManager.getConnection(url);

            // Create statement
            Statement stmt = conn.createStatement();

            // Execute query
            ResultSet rs = stmt.executeQuery(query);

            // Process result
            if (rs.next()) {
                int result = rs.getInt("result");
                System.out.println("Result of SELECT 1+1: " + result);
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}/*/