import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Clase para leer archivos XML y hacer consultas con XPath.
 */
public class LectorXML {

    /**
     * Muestra estadísticas de reservas leyendo el archivo reservas.xml con XPath
     */
    public static void estadisticasReservas() {
        try {
            File archivo = new File("reservas.xml");
            if (!archivo.exists()) {
                System.out.println("El archivo reservas.xml no existe. Exporta las reservas primero.");
                return;
            }

            // Cargar el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);

            // Crear XPath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            System.out.println("\n========================================");
            System.out.println("   ESTADISTICAS DE RESERVAS (XML)");
            System.out.println("========================================");

            // 1. Total de reservas
            XPathExpression expr = xpath.compile("count(//reserva)");
            Double totalReservas = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Total de reservas: " + totalReservas.intValue());

            // 2. Reservas activas
            expr = xpath.compile("count(//reserva[estado='activa'])");
            Double reservasActivas = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Reservas activas: " + reservasActivas.intValue());

            // 3. Reservas canceladas
            expr = xpath.compile("count(//reserva[estado='cancelada'])");
            Double reservasCanceladas = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Reservas canceladas: " + reservasCanceladas.intValue());

            // 4. Reservas por aula
            System.out.println("\n--- Reservas por aula ---");
            Map<String, Integer> reservasPorAula = new HashMap<>();
            
            expr = xpath.compile("//reserva/aula");
            NodeList aulas = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < aulas.getLength(); i++) {
                String aula = aulas.item(i).getTextContent();
                reservasPorAula.put(aula, reservasPorAula.getOrDefault(aula, 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : reservasPorAula.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " reservas");
            }

            // 5. Usuario con más reservas
            System.out.println("\n--- Usuarios con mas reservas ---");
            Map<String, Integer> reservasPorUsuario = new HashMap<>();
            
            expr = xpath.compile("//reserva/usuario_nombre");
            NodeList usuarios = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < usuarios.getLength(); i++) {
                String usuario = usuarios.item(i).getTextContent();
                reservasPorUsuario.put(usuario, reservasPorUsuario.getOrDefault(usuario, 0) + 1);
            }
            
            // Ordenar y mostrar top 5
            reservasPorUsuario.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " reservas"));

            // 6. Reservas de hoy
            String hoy = LocalDate.now().toString();
            expr = xpath.compile("count(//reserva[fecha='" + hoy + "'])");
            Double reservasHoy = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("\nReservas para hoy (" + hoy + "): " + reservasHoy.intValue());

        } catch (Exception e) {
            System.err.println("Error al leer reservas.xml");
            e.printStackTrace();
        }
    }

    /**
     * Muestra estadísticas de préstamos leyendo el archivo prestamos.xml con XPath
     */
    public static void estadisticasPrestamos() {
        try {
            File archivo = new File("prestamos.xml");
            if (!archivo.exists()) {
                System.out.println("El archivo prestamos.xml no existe. Exporta los prestamos primero.");
                return;
            }

            // Cargar el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);

            // Crear XPath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            System.out.println("\n========================================");
            System.out.println("   ESTADISTICAS DE PRESTAMOS (XML)");
            System.out.println("========================================");

            // 1. Total de préstamos
            XPathExpression expr = xpath.compile("count(//prestamo)");
            Double totalPrestamos = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Total de prestamos: " + totalPrestamos.intValue());

            // 2. Préstamos activos
            expr = xpath.compile("count(//prestamo[estado='activo'])");
            Double prestamosActivos = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Prestamos activos: " + prestamosActivos.intValue());

            // 3. Préstamos devueltos
            expr = xpath.compile("count(//prestamo[estado='devuelto'])");
            Double prestamosDevueltos = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("Prestamos devueltos: " + prestamosDevueltos.intValue());

            // 4. Dispositivos más prestados
            System.out.println("\n--- Dispositivos mas prestados ---");
            Map<String, Integer> prestamosPorDispositivo = new HashMap<>();
            
            expr = xpath.compile("//prestamo");
            NodeList prestamos = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            
            for (int i = 0; i < prestamos.getLength(); i++) {
                Node prestamo = prestamos.item(i);
                
                // Obtener tipo, marca y modelo del dispositivo
                String tipo = xpath.evaluate("dispositivo_tipo", prestamo);
                String marca = xpath.evaluate("dispositivo_marca", prestamo);
                String modelo = xpath.evaluate("dispositivo_modelo", prestamo);
                String dispositivo = tipo + " " + marca + " " + modelo;
                
                prestamosPorDispositivo.put(dispositivo, prestamosPorDispositivo.getOrDefault(dispositivo, 0) + 1);
            }
            
            // Ordenar y mostrar top 5
            prestamosPorDispositivo.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " prestamos"));

            // 5. Préstamos por tipo de dispositivo
            System.out.println("\n--- Prestamos por tipo ---");
            expr = xpath.compile("count(//prestamo[dispositivo_tipo='Ordenador'])");
            Double ordenadores = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("  Ordenadores: " + ordenadores.intValue());
            
            expr = xpath.compile("count(//prestamo[dispositivo_tipo='Tablet'])");
            Double tablets = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
            System.out.println("  Tablets: " + tablets.intValue());

        } catch (Exception e) {
            System.err.println("Error al leer prestamos.xml");
            e.printStackTrace();
        }
    }

    /**
     * Muestra el listado de usuarios sancionados leyendo directamente de la BD
     */
    public static void verSancionados() {
        System.out.println("\n========================================");
        System.out.println("      USUARIOS SANCIONADOS");
        System.out.println("========================================");
        
        try {
            java.sql.Connection conn = ConexionBD.getConexion();
            String sql = "SELECT u.id, u.nombre, u.apellido1, u.dni, s.dias_retraso, s.fecha_sancion " +
                         "FROM usuarios u " +
                         "JOIN sanciones s ON u.id = s.id_usuario " +
                         "WHERE u.estado = 'sancionado' " +
                         "ORDER BY s.fecha_sancion DESC";
            
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            boolean haySancionados = false;
            while (rs.next()) {
                haySancionados = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre") + " " + rs.getString("apellido1");
                int dni = rs.getInt("dni");
                int diasRetraso = rs.getInt("dias_retraso");
                String fechaSancion = rs.getDate("fecha_sancion").toString();

                System.out.printf("Usuario ID=%d | %s | DNI=%d | Retraso=%d dias | Sancionado el %s\n",
                        id, nombre, dni, diasRetraso, fechaSancion);
            }

            if (!haySancionados) {
                System.out.println("No hay usuarios sancionados.");
            }

        } catch (Exception e) {
            System.err.println("Error al consultar sancionados.");
            e.printStackTrace();
        }
    }
}