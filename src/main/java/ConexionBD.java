import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para reutilizar la misma conexión.
 */
public class ConexionBD {

    // Configuración de la base de datos
    private static final String URL = "jdbc:mariadb://127.0.0.1:3307/gestion_aulas?user=guti&password=guti";
    private static final String USER = "guti";       // Cambia esto según tu configuración
    private static final String PASSWORD = "guti";   // Cambia esto según tu configuración

    private static Connection conexion = null;

    /**
     * Obtiene la conexión a la base de datos.
     * Si no existe, la crea. Si ya existe, la reutiliza.
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de MySQL
                Class.forName("org.mariadb.jdbc.Driver");
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Conexión establecida con la base de datos.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
            System.err.println("Asegúrate de tener el .jar de MySQL Connector en el classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    /**
     * Cierra la conexión con la base de datos.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✓ Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }
}