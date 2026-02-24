import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Clase para exportar datos de la base de datos a archivos XML.
 */
public class ExportadorXML {

    /**
     * Exporta todas las reservas de la BD a un archivo reservas.xml
     */
    public static void exportarReservas() {
        try {
            // Crear el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz
            Element raiz = doc.createElement("reservas");
            doc.appendChild(raiz);

            // Consultar las reservas de la BD
            Connection conn = ConexionBD.getConexion();
            String sql = "SELECT r.*, u.nombre, u.apellido1, u.dni, p.numero_puesto, p.aula, p.tipo_puesto " +
                         "FROM reservas r " +
                         "JOIN usuarios u ON r.id_usuario = u.id " +
                         "JOIN puestos p ON r.id_puesto = p.id " +
                         "ORDER BY r.fecha DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Crear un elemento <reserva> por cada fila
            while (rs.next()) {
                Element reserva = doc.createElement("reserva");
                raiz.appendChild(reserva);

                // Añadir atributo id
                reserva.setAttribute("id", String.valueOf(rs.getInt("id")));

                // Crear elementos hijos
                agregarElemento(doc, reserva, "usuario_id", String.valueOf(rs.getInt("id_usuario")));
                agregarElemento(doc, reserva, "usuario_nombre", rs.getString("nombre") + " " + rs.getString("apellido1"));
                agregarElemento(doc, reserva, "usuario_dni", String.valueOf(rs.getInt("dni")));
                agregarElemento(doc, reserva, "puesto_id", String.valueOf(rs.getInt("id_puesto")));
                agregarElemento(doc, reserva, "puesto_numero", String.valueOf(rs.getInt("numero_puesto")));
                agregarElemento(doc, reserva, "aula", rs.getString("aula"));
                agregarElemento(doc, reserva, "tipo_puesto", rs.getString("tipo_puesto"));
                agregarElemento(doc, reserva, "fecha", rs.getDate("fecha").toString());
                agregarElemento(doc, reserva, "hora_inicio", rs.getTime("hora_inicio").toString());
                agregarElemento(doc, reserva, "hora_fin", rs.getTime("hora_fin").toString());
                agregarElemento(doc, reserva, "estado", rs.getString("estado"));
            }

            // Guardar el documento XML en un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("reservas.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo reservas.xml generado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al exportar reservas a XML.");
            e.printStackTrace();
        }
    }

    /**
     * Exporta todos los préstamos de la BD a un archivo prestamos.xml
     */
    public static void exportarPrestamos() {
        try {
            // Crear el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz
            Element raiz = doc.createElement("prestamos");
            doc.appendChild(raiz);

            // Consultar los préstamos de la BD
            Connection conn = ConexionBD.getConexion();
            String sql = "SELECT p.*, u.nombre, u.apellido1, u.dni, d.marca, d.modelo, d.tipo " +
                         "FROM prestamos p " +
                         "JOIN usuarios u ON p.id_usuario = u.id " +
                         "JOIN dispositivos d ON p.id_dispositivo = d.id " +
                         "ORDER BY p.fecha_prestamo DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Crear un elemento <prestamo> por cada fila
            while (rs.next()) {
                Element prestamo = doc.createElement("prestamo");
                raiz.appendChild(prestamo);

                // Añadir atributo id
                prestamo.setAttribute("id", String.valueOf(rs.getInt("id")));

                // Crear elementos hijos
                agregarElemento(doc, prestamo, "usuario_id", String.valueOf(rs.getInt("id_usuario")));
                agregarElemento(doc, prestamo, "usuario_nombre", rs.getString("nombre") + " " + rs.getString("apellido1"));
                agregarElemento(doc, prestamo, "usuario_dni", String.valueOf(rs.getInt("dni")));
                agregarElemento(doc, prestamo, "dispositivo_id", String.valueOf(rs.getInt("id_dispositivo")));
                agregarElemento(doc, prestamo, "dispositivo_tipo", rs.getString("tipo"));
                agregarElemento(doc, prestamo, "dispositivo_marca", rs.getString("marca"));
                agregarElemento(doc, prestamo, "dispositivo_modelo", rs.getString("modelo"));
                agregarElemento(doc, prestamo, "fecha_prestamo", rs.getDate("fecha_prestamo").toString());
                agregarElemento(doc, prestamo, "fecha_devolucion_prevista", rs.getDate("fecha_devolucion_prevista").toString());
                
                Date fechaReal = rs.getDate("fecha_devolucion_real");
                agregarElemento(doc, prestamo, "fecha_devolucion_real", fechaReal != null ? fechaReal.toString() : "");
                
                agregarElemento(doc, prestamo, "estado", rs.getString("estado"));
            }

            // Guardar el documento XML en un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("prestamos.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo prestamos.xml generado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al exportar prestamos a XML.");
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para añadir un elemento hijo con texto a un elemento padre
     */
    private static void agregarElemento(Document doc, Element padre, String nombre, String valor) {
        Element elemento = doc.createElement(nombre);
        elemento.appendChild(doc.createTextNode(valor));
        padre.appendChild(elemento);
    }
}