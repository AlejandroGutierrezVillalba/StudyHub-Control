import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * DAO para gestionar préstamos de dispositivos en la BD.
 */
public class PrestamoDAO {

    /**
     * Registra un nuevo préstamo de dispositivo.
     */
    public static void registrar(Scanner sc) {
        System.out.println("\n-- REGISTRAR PRESTAMO --");

        System.out.print("ID de usuario: ");
        int idUsuario = leerEntero(sc);

        Usuario usuario = UsuarioDAO.buscarPorID(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        if ("sancionado".equalsIgnoreCase(usuario.getEstado())) {
            System.out.println("Usuario sancionado. No puede realizar prestamos.");
            return;
        }
        if (!usuario.isActivo()) {
            System.out.println("Usuario de baja. No puede realizar prestamos.");
            return;
        }

        DispositivoDAO.verDisponibles();
        System.out.print("ID del dispositivo: ");
        int idDispositivo = leerEntero(sc);

        Dispositivo dispositivo = DispositivoDAO.buscarPorID(idDispositivo);
        if (dispositivo == null) {
            System.out.println("Dispositivo no encontrado.");
            return;
        }
        if (!dispositivo.isDisponible()) {
            System.out.println("Ese dispositivo no esta disponible.");
            return;
        }

        System.out.print("Fecha de devolucion prevista (AAAA-MM-DD): ");
        LocalDate fechaDevolucion = leerFecha(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "INSERT INTO prestamos (id_usuario, id_dispositivo, fecha_prestamo, fecha_devolucion_prevista, estado) VALUES (?, ?, ?, ?, 'activo')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idDispositivo);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(fechaDevolucion));
            stmt.executeUpdate();

            // Marcar dispositivo como prestado
            DispositivoDAO.cambiarEstado(idDispositivo, "prestado");

            System.out.println("Prestamo registrado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al registrar prestamo.");
            e.printStackTrace();
        }
    }

    /**
     * Registra la devolución de un dispositivo prestado.
     */
    public static void devolver(Scanner sc) {
        System.out.println("\n-- REGISTRAR DEVOLUCION --");
        verActivos();

        System.out.print("ID del prestamo a devolver: ");
        int idPrestamo = leerEntero(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            // Obtener datos del préstamo
            String getSQL = "SELECT id_dispositivo, id_usuario, fecha_devolucion_prevista FROM prestamos WHERE id = ? AND estado = 'activo'";
            PreparedStatement getStmt = conn.prepareStatement(getSQL);
            getStmt.setInt(1, idPrestamo);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Prestamo no encontrado o ya devuelto.");
                return;
            }

            int idDispositivo = rs.getInt("id_dispositivo");
            int idUsuario = rs.getInt("id_usuario");
            LocalDate fechaPrevista = rs.getDate("fecha_devolucion_prevista").toLocalDate();
            LocalDate fechaReal = LocalDate.now();

            // Calcular retraso
            long diasRetraso = fechaReal.toEpochDay() - fechaPrevista.toEpochDay();
            diasRetraso = Math.max(0, diasRetraso);

            // Actualizar el préstamo
            String sql = "UPDATE prestamos SET fecha_devolucion_real = ?, estado = 'devuelto' WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(fechaReal));
            stmt.setInt(2, idPrestamo);
            stmt.executeUpdate();

            // Liberar el dispositivo
            DispositivoDAO.cambiarEstado(idDispositivo, "disponible");

            if (diasRetraso > 0) {
                System.out.println("Devolucion con " + diasRetraso + " dias de retraso.");
                
                // Sancionar al usuario
                String updateSQL = "UPDATE usuarios SET estado = 'sancionado' WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                updateStmt.setInt(1, idUsuario);
                updateStmt.executeUpdate();

                // Registrar la sanción
                String sancionSQL = "INSERT INTO sanciones (id_usuario, id_prestamo, dias_retraso, fecha_sancion) VALUES (?, ?, ?, ?)";
                PreparedStatement sancionStmt = conn.prepareStatement(sancionSQL);
                sancionStmt.setInt(1, idUsuario);
                sancionStmt.setInt(2, idPrestamo);
                sancionStmt.setLong(3, diasRetraso);
                sancionStmt.setDate(4, Date.valueOf(fechaReal));
                sancionStmt.executeUpdate();

                System.out.println("El usuario ha sido sancionado.");
            } else {
                System.out.println("Devolucion registrada correctamente. Sin retraso.");
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar devolucion.");
            e.printStackTrace();
        }
    }

    /**
     * Muestra todos los préstamos activos.
     */
    public static void verActivos() {
        System.out.println("\n-- PRESTAMOS ACTIVOS --");
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT p.*, u.nombre, u.apellido1, d.marca, d.modelo, d.tipo " +
                         "FROM prestamos p " +
                         "JOIN usuarios u ON p.id_usuario = u.id " +
                         "JOIN dispositivos d ON p.id_dispositivo = d.id " +
                         "WHERE p.estado = 'activo' " +
                         "ORDER BY p.fecha_devolucion_prevista ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean hayPrestamos = false;
            while (rs.next()) {
                hayPrestamos = true;
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre") + " " + rs.getString("apellido1");
                String dispositivo = rs.getString("tipo") + " " + rs.getString("marca") + " " + rs.getString("modelo");
                LocalDate fechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaPrevista = rs.getDate("fecha_devolucion_prevista").toLocalDate();

                // Calcular si tiene retraso
                long diasRetraso = LocalDate.now().toEpochDay() - fechaPrevista.toEpochDay();
                diasRetraso = Math.max(0, diasRetraso);
                String retraso = (diasRetraso > 0) ? " [RETRASO: " + diasRetraso + " dias]" : "";

                System.out.printf("Prestamo ID=%d | Usuario=%s | Dispositivo=%s | Prestado=%s | Previsto=%s%s\n",
                        id, nombreUsuario, dispositivo, fechaPrestamo, fechaPrevista, retraso);
            }

            if (!hayPrestamos) {
                System.out.println("No hay prestamos activos.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar prestamos activos.");
            e.printStackTrace();
        }
    }

    /**
     * Muestra solo los préstamos que tienen retraso.
     */
    public static void verRetrasados() {
        System.out.println("\n-- PRESTAMOS CON RETRASO --");
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT p.*, u.nombre, u.apellido1, d.marca, d.modelo, d.tipo " +
                         "FROM prestamos p " +
                         "JOIN usuarios u ON p.id_usuario = u.id " +
                         "JOIN dispositivos d ON p.id_dispositivo = d.id " +
                         "WHERE p.estado = 'activo' AND p.fecha_devolucion_prevista < CURDATE() " +
                         "ORDER BY p.fecha_devolucion_prevista ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean hayRetrasados = false;
            while (rs.next()) {
                hayRetrasados = true;
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre") + " " + rs.getString("apellido1");
                String dispositivo = rs.getString("tipo") + " " + rs.getString("marca") + " " + rs.getString("modelo");
                LocalDate fechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaPrevista = rs.getDate("fecha_devolucion_prevista").toLocalDate();

                long diasRetraso = LocalDate.now().toEpochDay() - fechaPrevista.toEpochDay();

                System.out.printf("Prestamo ID=%d | Usuario=%s | Dispositivo=%s | Prestado=%s | Previsto=%s | RETRASO: %d dias\n",
                        id, nombreUsuario, dispositivo, fechaPrestamo, fechaPrevista, diasRetraso);
            }

            if (!hayRetrasados) {
                System.out.println("No hay prestamos con retraso.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar prestamos retrasados.");
            e.printStackTrace();
        }
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un numero valido: ");
            }
        }
    }

    private static LocalDate leerFecha(Scanner sc) {
        while (true) {
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.print("Formato incorrecto. Usa AAAA-MM-DD: ");
            }
        }
    }
}