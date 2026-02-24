import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * DAO para gestionar reservas de puestos en la BD.
 */
public class ReservaDAO {

    /**
     * Crea una nueva reserva de puesto.
     */
    public static void reservar(Scanner sc) {
        System.out.println("\n-- RESERVAR PUESTO --");

        System.out.print("Tu ID de usuario: ");
        int idUsuario = leerEntero(sc);

        Usuario usuario = UsuarioDAO.buscarPorID(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        if (!usuario.isActivo()) {
            System.out.println("Tu cuenta esta de baja o sancionada. No puedes reservar.");
            return;
        }

        PuestoDAO.verPorSala();
        System.out.print("ID del puesto que quieres reservar: ");
        int idPuesto = leerEntero(sc);

        Puesto puesto = PuestoDAO.buscarPorID(idPuesto);
        if (puesto == null) {
            System.out.println("Puesto no encontrado.");
            return;
        }
        if (puesto.isEstaReservado()) {
            System.out.println("Ese puesto ya esta ocupado.");
            return;
        }

        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = leerFecha(sc);
        System.out.print("Hora inicio (HH:MM): ");
        LocalTime horaInicio = leerHora(sc);
        System.out.print("Hora fin (HH:MM): ");
        LocalTime horaFin = leerHora(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            // Comprobar que no haya ya una reserva activa en ese puesto, fecha y hora
            String checkSQL = "SELECT COUNT(*) FROM reservas WHERE id_puesto = ? AND fecha = ? AND hora_inicio = ? AND estado = 'activa'";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, idPuesto);
            checkStmt.setDate(2, Date.valueOf(fecha));
            checkStmt.setTime(3, Time.valueOf(horaInicio));
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Ese puesto ya esta reservado en esa franja horaria.");
                return;
            }

            // Insertar la reserva
            String sql = "INSERT INTO reservas (id_usuario, id_puesto, fecha, hora_inicio, hora_fin, estado) VALUES (?, ?, ?, ?, ?, 'activa')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPuesto);
            stmt.setDate(3, Date.valueOf(fecha));
            stmt.setTime(4, Time.valueOf(horaInicio));
            stmt.setTime(5, Time.valueOf(horaFin));
            stmt.executeUpdate();

            // Marcar el puesto como reservado
            PuestoDAO.cambiarEstado(idPuesto, true);

            System.out.println("Reserva realizada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al crear reserva.");
            e.printStackTrace();
        }
    }

    /**
     * Cancela una reserva activa.
     */
    public static void cancelar(Scanner sc) {
        System.out.println("\n-- CANCELAR RESERVA --");
        System.out.print("Tu ID de usuario: ");
        int idUsuario = leerEntero(sc);

        verDeUsuario(idUsuario);

        System.out.print("ID de la reserva a cancelar: ");
        int idReserva = leerEntero(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            // Obtener la reserva para liberar el puesto
            String getSQL = "SELECT id_puesto FROM reservas WHERE id = ? AND id_usuario = ? AND estado = 'activa'";
            PreparedStatement getStmt = conn.prepareStatement(getSQL);
            getStmt.setInt(1, idReserva);
            getStmt.setInt(2, idUsuario);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Reserva no encontrada o ya cancelada.");
                return;
            }

            int idPuesto = rs.getInt("id_puesto");

            // Cancelar la reserva
            String sql = "UPDATE reservas SET estado = 'cancelada' WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            stmt.executeUpdate();

            // Liberar el puesto
            PuestoDAO.cambiarEstado(idPuesto, false);

            System.out.println("Reserva cancelada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al cancelar reserva.");
            e.printStackTrace();
        }
    }

    /**
     * Muestra todas las reservas de un usuario.
     */
    public static void verDeUsuario(Scanner sc) {
        System.out.println("\n-- MIS RESERVAS --");
        System.out.print("Tu ID de usuario: ");
        int idUsuario = leerEntero(sc);
        verDeUsuario(idUsuario);
    }

    /**
     * Muestra las reservas de un usuario específico (método interno).
     */
    private static void verDeUsuario(int idUsuario) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT r.*, p.numero_puesto, p.aula, p.tipo_puesto " +
                         "FROM reservas r " +
                         "JOIN puestos p ON r.id_puesto = p.id " +
                         "WHERE r.id_usuario = ? " +
                         "ORDER BY r.fecha DESC, r.hora_inicio DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            boolean hayReservas = false;
            while (rs.next()) {
                hayReservas = true;
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime horaIni = rs.getTime("hora_inicio").toLocalTime();
                LocalTime horaFin = rs.getTime("hora_fin").toLocalTime();
                String estado = rs.getString("estado");
                int numPuesto = rs.getInt("numero_puesto");
                String aula = rs.getString("aula");

                System.out.printf("Reserva ID=%d | Puesto #%d en %s | Fecha=%s | %s-%s | Estado=%s\n",
                        id, numPuesto, aula, fecha, horaIni, horaFin, estado);
            }

            if (!hayReservas) {
                System.out.println("No tienes reservas registradas.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar reservas.");
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

    private static LocalTime leerHora(Scanner sc) {
        while (true) {
            try {
                return LocalTime.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.print("Formato incorrecto. Usa HH:MM: ");
            }
        }
    }
}