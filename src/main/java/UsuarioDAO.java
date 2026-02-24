import java.sql.*;
import java.util.Scanner;

/**
 * DAO (Data Access Object) para gestionar operaciones con usuarios en la BD.
 */
public class UsuarioDAO {

    /**
     * Añade un nuevo usuario (alumno o profesor) a la base de datos.
     */
    public static void anadir(Scanner sc) {
        System.out.println("\n-- ANADIR USUARIO --");
        System.out.print("Tipo (1=Alumno / 2=Profesor): ");
        int tipo = leerEntero(sc);

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido 1: ");
        String ap1 = sc.nextLine();
        System.out.print("Apellido 2: ");
        String ap2 = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("DNI: ");
        int dni = leerEntero(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            // Comprobar que el correo o DNI no existan ya
            String checkSQL = "SELECT COUNT(*) FROM usuarios WHERE correo = ? OR dni = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setString(1, correo);
            checkStmt.setInt(2, dni);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Error: ese correo o DNI ya esta registrado.");
                return;
            }

            // Insertar el usuario
            String sql = "INSERT INTO usuarios (nombre, apellido1, apellido2, correo, dni, tipo, estado, curso, tutor, edad, materia) VALUES (?, ?, ?, ?, ?, ?, 'alta', ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, ap1);
            stmt.setString(3, ap2);
            stmt.setString(4, correo);
            stmt.setInt(5, dni);

            if (tipo == 1) {
                stmt.setString(6, "alumno");
                System.out.print("Curso: ");
                String curso = sc.nextLine();
                System.out.print("Tutor: ");
                String tutor = sc.nextLine();
                System.out.print("Edad: ");
                int edad = leerEntero(sc);
                stmt.setString(7, curso);
                stmt.setString(8, tutor);
                stmt.setInt(9, edad);
                stmt.setNull(10, Types.VARCHAR);  // materia = null
            } else if (tipo == 2) {
                stmt.setString(6, "profesor");
                System.out.print("Materia: ");
                String materia = sc.nextLine();
                stmt.setNull(7, Types.VARCHAR);   // curso = null
                stmt.setNull(8, Types.VARCHAR);   // tutor = null
                stmt.setNull(9, Types.INTEGER);   // edad = null
                stmt.setString(10, materia);
            } else {
                System.out.println("Tipo no valido.");
                return;
            }

            stmt.executeUpdate();
            System.out.println("Usuario anadido correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al anadir usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Lista todos los usuarios de la base de datos.
     */
    public static void listar() {
        System.out.println("\n-- LISTADO DE USUARIOS --");
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT * FROM usuarios";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean hayUsuarios = false;
            while (rs.next()) {
                hayUsuarios = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String ap1 = rs.getString("apellido1");
                String ap2 = rs.getString("apellido2");
                String correo = rs.getString("correo");
                int dni = rs.getInt("dni");
                String tipo = rs.getString("tipo");
                String estado = rs.getString("estado");

                System.out.printf("[%s] ID=%d | %s %s %s | DNI=%d | Email=%s | Estado=%s",
                        tipo.toUpperCase(), id, nombre, ap1, ap2, dni, correo, estado);

                if ("alumno".equals(tipo)) {
                    String curso = rs.getString("curso");
                    String tutor = rs.getString("tutor");
                    int edad = rs.getInt("edad");
                    System.out.printf(" | Curso=%s | Tutor=%s | Edad=%d", curso, tutor, edad);
                } else if ("profesor".equals(tipo)) {
                    String materia = rs.getString("materia");
                    System.out.printf(" | Materia=%s", materia);
                }
                System.out.println();
            }

            if (!hayUsuarios) {
                System.out.println("No hay usuarios registrados.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios.");
            e.printStackTrace();
        }
    }

    /**
     * Modifica los datos de un usuario existente.
     */
    public static void modificar(Scanner sc) {
        System.out.println("\n-- MODIFICAR USUARIO --");
        listar();
        System.out.print("ID del usuario a modificar: ");
        int id = leerEntero(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            // Verificar que el usuario existe
            String checkSQL = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Usuario no encontrado.");
                return;
            }

            String correoActual = rs.getString("correo");
            String nombreActual = rs.getString("nombre");

            System.out.print("Nuevo correo (Enter para mantener '" + correoActual + "'): ");
            String correo = sc.nextLine();
            if (correo.isBlank()) correo = correoActual;

            System.out.print("Nuevo nombre (Enter para mantener '" + nombreActual + "'): ");
            String nombre = sc.nextLine();
            if (nombre.isBlank()) nombre = nombreActual;

            String sql = "UPDATE usuarios SET nombre = ?, correo = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setInt(3, id);
            stmt.executeUpdate();

            System.out.println("Usuario modificado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al modificar usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Da de baja un usuario (cambia su estado a 'baja').
     */
    public static void darDeBaja(Scanner sc) {
        System.out.println("\n-- DAR DE BAJA USUARIO --");
        listar();
        System.out.print("ID del usuario a dar de baja: ");
        int id = leerEntero(sc);

        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "UPDATE usuarios SET estado = 'baja' WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario dado de baja correctamente.");
            } else {
                System.out.println("Usuario no encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Error al dar de baja usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Busca un usuario por ID y devuelve sus datos básicos (para usar en otros DAOs).
     */
    public static Usuario buscarPorID(int id) {
        Connection conn = ConexionBD.getConexion();
        try {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                String nombre = rs.getString("nombre");
                String ap1 = rs.getString("apellido1");
                String ap2 = rs.getString("apellido2");
                String correo = rs.getString("correo");
                String estado = rs.getString("estado");
                int dni = rs.getInt("dni");

                if ("alumno".equals(tipo)) {
                    String curso = rs.getString("curso");
                    String tutor = rs.getString("tutor");
                    int edad = rs.getInt("edad");
                    return new Alumno(nombre, ap1, ap2, correo, estado, id, dni, curso, tutor, edad);
                } else {
                    String materia = rs.getString("materia");
                    return new Profesor(nombre, ap1, ap2, correo, estado, id, dni, materia);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID.");
            e.printStackTrace();
        }
        return null;
    }

    // ============================================================
    // MÉTODO AUXILIAR
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
}