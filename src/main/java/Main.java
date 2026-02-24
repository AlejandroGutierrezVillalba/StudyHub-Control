import java.util.Scanner;

//Clase principal. Solo gestiona la navegación por menús.Toda la lógica de negocio irá en los DAOs correspondientes.//

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        menuPrincipal();
    }

    // ============================================================
    // MENÚ PRINCIPAL
    // ============================================================

    static void menuPrincipal() {
        int opcion = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("     SISTEMA DE GESTION DE AULAS");
            System.out.println("========================================");
            System.out.println("  1. Gestion de Usuarios");
            System.out.println("  2. Reserva de Puestos");
            System.out.println("  3. Prestamo de Dispositivos");
            System.out.println("  4. Informes y Estadisticas");
            System.out.println("  0. Salir");
            System.out.println("========================================");
            System.out.print("Elige una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuUsuarios();
                case 2 -> menuReservas();
                case 3 -> menuPrestamos();
                case 4 -> menuInformes();
                case 0 -> System.out.println("\nHasta luego!");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ============================================================
    // MENÚ GESTIÓN DE USUARIOS
    // ============================================================
    static void menuUsuarios() {
        int opcion = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("        GESTION DE USUARIOS");
            System.out.println("========================================");
            System.out.println("  1. Anadir usuario");
            System.out.println("  2. Listar usuarios");
            System.out.println("  3. Modificar usuario");
            System.out.println("  4. Dar de baja usuario");
            System.out.println("  0. Volver");
            System.out.println("========================================");
            System.out.print("Elige una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> UsuarioDAO.anadir(sc);
                case 2 -> UsuarioDAO.listar();
                case 3 -> UsuarioDAO.modificar(sc);
                case 4 -> UsuarioDAO.darDeBaja(sc);
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ============================================================
    // MENÚ RESERVA DE PUESTOS
    // ============================================================
    static void menuReservas() {
        int opcion = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("        RESERVA DE PUESTOS");
            System.out.println("========================================");
            System.out.println("  1. Ver puestos por sala");
            System.out.println("  2. Reservar un puesto");
            System.out.println("  3. Cancelar una reserva");
            System.out.println("  4. Ver mis reservas");
            System.out.println("  0. Volver");
            System.out.println("========================================");
            System.out.print("Elige una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> PuestoDAO.verPorSala();
                case 2 -> ReservaDAO.reservar(sc);
                case 3 -> ReservaDAO.cancelar(sc);
                case 4 -> ReservaDAO.verDeUsuario(sc);
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ============================================================
    // MENÚ PRÉSTAMO DE DISPOSITIVOS
    // ============================================================
    static void menuPrestamos() {
        int opcion = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("      PRESTAMO DE DISPOSITIVOS");
            System.out.println("========================================");
            System.out.println("  1. Ver dispositivos disponibles");
            System.out.println("  2. Registrar prestamo");
            System.out.println("  3. Registrar devolucion");
            System.out.println("  4. Ver prestamos activos");
            System.out.println("  5. Ver prestamos con retraso");
            System.out.println("  0. Volver");
            System.out.println("========================================");
            System.out.print("Elige una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> DispositivoDAO.verDisponibles();
                case 2 -> PrestamoDAO.registrar(sc);
                case 3 -> PrestamoDAO.devolver(sc);
                case 4 -> PrestamoDAO.verActivos();
                case 5 -> PrestamoDAO.verRetrasados();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ============================================================
    // MENÚ INFORMES Y ESTADÍSTICAS
    // ============================================================
    static void menuInformes() {
        int opcion = -1;
        do {
            System.out.println("\n========================================");
            System.out.println("      INFORMES Y ESTADISTICAS");
            System.out.println("========================================");
            System.out.println("  1. Exportar reservas a XML");
            System.out.println("  2. Exportar prestamos a XML");
            System.out.println("  3. Ver estadisticas de reservas (XML)");
            System.out.println("  4. Ver estadisticas de prestamos (XML)");
            System.out.println("  5. Ver usuarios sancionados");
            System.out.println("  0. Volver");
            System.out.println("========================================");
            System.out.print("Elige una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> ExportadorXML.exportarReservas();
                case 2 -> ExportadorXML.exportarPrestamos();
                case 3 -> LectorXML.estadisticasReservas();
                case 4 -> LectorXML.estadisticasPrestamos();
                case 5 -> LectorXML.verSancionados();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ============================================================
    // LECTURA SEGURA DE ENTEROS
    // ============================================================
    static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un numero valido: ");
            }
        }
    }
}