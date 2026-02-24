import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO para gestionar operaciones con dispositivos en la BD.
 */
public class DispositivoDAO {

    /**
     * Muestra todos los dispositivos disponibles para préstamo.
     */
    public static void verDisponibles() {
        System.out.println("\n-- DISPOSITIVOS DISPONIBLES --");
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT * FROM dispositivos WHERE estado = 'disponible'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean hayDispositivos = false;
            while (rs.next()) {
                hayDispositivos = true;
                int id = rs.getInt("id");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                String tipo = rs.getString("tipo");
                System.out.printf("[%s] ID=%d | %s %s\n", tipo, id, marca, modelo);
            }

            if (!hayDispositivos) {
                System.out.println("No hay dispositivos disponibles.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar dispositivos.");
            e.printStackTrace();
        }
    }

    /**
     * Busca un dispositivo por ID y devuelve un objeto Dispositivo.
     */
    public static Dispositivo buscarPorID(int id) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT * FROM dispositivos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                String tipo = rs.getString("tipo");
                String estado = rs.getString("estado");

                if ("Ordenador".equals(tipo)) {
                    return new Ordenador(marca, modelo, tipo, id, estado);
                } else {
                    return new Tablet(marca, modelo, tipo, id, estado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar dispositivo por ID.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cambia el estado de un dispositivo (disponible, prestado, averiado).
     */
    public static void cambiarEstado(int idDispositivo, String nuevoEstado) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "UPDATE dispositivos SET estado = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idDispositivo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del dispositivo.");
            e.printStackTrace();
        }
    }
}