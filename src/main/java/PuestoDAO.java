import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con puestos en la BD.
 */
public class PuestoDAO {

    /**
     * Muestra todos los puestos agrupados por sala/aula.
     */
    public static void verPorSala() {
        System.out.println("\n-- PUESTOS POR SALA --");
        Connection conn = ConexionBD.getConexion();
        try {
            // Obtener todas las aulas únicas
            String sqlAulas = "SELECT DISTINCT aula FROM puestos ORDER BY aula";
            Statement stmtAulas = conn.createStatement();
            ResultSet rsAulas = stmtAulas.executeQuery(sqlAulas);

            boolean hayPuestos = false;
            while (rsAulas.next()) {
                hayPuestos = true;
                String aula = rsAulas.getString("aula");
                System.out.println("\n  Sala: " + aula);

                // Obtener puestos de esa aula
                String sqlPuestos = "SELECT * FROM puestos WHERE aula = ?";
                PreparedStatement stmtPuestos = conn.prepareStatement(sqlPuestos);
                stmtPuestos.setString(1, aula);
                ResultSet rsPuestos = stmtPuestos.executeQuery();

                while (rsPuestos.next()) {
                    int id = rsPuestos.getInt("id");
                    int numPuesto = rsPuestos.getInt("numero_puesto");
                    String tipo = rsPuestos.getString("tipo_puesto");
                    boolean reservado = rsPuestos.getBoolean("esta_reservado");
                    String estado = reservado ? "OCUPADO" : "LIBRE";
                    System.out.printf("     Puesto #%d (ID=%d) | Tipo=%s | %s\n",
                            numPuesto, id, tipo, estado);
                }
            }

            if (!hayPuestos) {
                System.out.println("No hay puestos registrados.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar puestos.");
            e.printStackTrace();
        }
    }

    /**
     * Busca un puesto por ID y devuelve un objeto Puesto.
     */
    public static Puesto buscarPorID(int id) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT * FROM puestos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int numPuesto = rs.getInt("numero_puesto");
                String aula = rs.getString("aula");
                String tipo = rs.getString("tipo_puesto");
                boolean reservado = rs.getBoolean("esta_reservado");
                return new Puesto(id, numPuesto, aula, tipo, reservado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar puesto por ID.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Marca un puesto como reservado o libre.
     */
    public static void cambiarEstado(int idPuesto, boolean reservado) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "UPDATE puestos SET esta_reservado = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, reservado);
            stmt.setInt(2, idPuesto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del puesto.");
            e.printStackTrace();
        }
    }
}