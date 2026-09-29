import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Ejercicio03 {
    public static void main(String[] args) {
        try {
            // 1. Aquí se crea la instancia de DocumentBuilderFactory para construir el parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 2. Aquí se carga el documento XML desde el fichero
            Document doc = builder.parse(new File("Contactos.xml"));
            doc.getDocumentElement().normalize(); // Normaliza la estructura

            // 3. Aquí se crea una lista con todos los elementos "contacto"
            NodeList listaNodos = doc.getElementsByTagName("contacto");

            System.out.println("--- LISTADO DE CONTACTOS ---");
            // 4. Aquí se recorre el NodeList usando un bucle para mostrar su contenido
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    // Extraemos el texto de cada etiqueta hija
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    String apellidos = elemento.getElementsByTagName("apellidos").item(0).getTextContent();
                    String email = elemento.getElementsByTagName("email").item(0).getTextContent();
                    String telefono = elemento.getElementsByTagName("telefono").item(0).getTextContent();

                    System.out.println("Nombre: " + nombre + " " + apellidos);
                    System.out.println("Email: " + email + " | Tel: " + telefono);
                    System.out.println("--------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}