import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Ejercicio05 {
    public static void main(String[] args) {
        try {
            // 1. Cargar el documento
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("Contactos.xml"));
            doc.getDocumentElement().normalize();

            // 2. Obtener todos los nodos de contacto
            NodeList listaContactos = doc.getElementsByTagName("contacto");

            System.out.println("--- LISTADO DE CONTACTOS ---");

            // 3. Recorrer la lista
            for (int i = 0; i < listaContactos.getLength(); i++) {
                Node nodo = listaContactos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    // Lectura de nodos hijos por etiqueta
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    String apellidos = elemento.getElementsByTagName("apellidos").item(0).getTextContent();
                    String email = elemento.getElementsByTagName("email").item(0).getTextContent();
                    String telefono = elemento.getElementsByTagName("telefono").item(0).getTextContent();

                    System.out.println("Nombre: " + nombre + " " + apellidos);
                    System.out.println("Email: " + email);
                    System.out.println("Teléfono: " + telefono);
                    System.out.println("--------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el XML: " + e.getMessage());
        }
    }
}