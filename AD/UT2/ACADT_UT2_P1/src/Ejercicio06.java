import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Ejercicio06 {
    public static void main(String[] args) {
        try {
            // 1. Configuración del parser y carga del documento Libros.xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File("Libros.xml"));
            doc.getDocumentElement().normalize();

            // 2. Obtención de todos los nodos con etiqueta "libro"
            NodeList listaLibros = doc.getElementsByTagName("libro");

            System.out.println("--- LISTADO DE LIBROS (DOM) ---");

            // 3. Recorrido de la lista de nodos
            for (int i = 0; i < listaLibros.getLength(); i++) {
                Node nodo = listaLibros.item(i);

                // Filtramos para asegurar que es un nodo elemento
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    // CLAVE DEL EJERCICIO: Lectura del atributo ISBN directamente
                    String isbn = elemento.getAttribute("ISBN");

                    // Lectura de los nodos hijos por su nombre de etiqueta
                    String titulo = elemento.getElementsByTagName("titulo").item(0).getTextContent();
                    String autor = elemento.getElementsByTagName("autor").item(0).getTextContent();
                    String editorial = elemento.getElementsByTagName("editorial").item(0).getTextContent();

                    // Mostrar resultados
                    System.out.println("ISBN: " + isbn);
                    System.out.println("Título: " + titulo);
                    System.out.println("Autor: " + autor + " | Editorial: " + editorial);
                    System.out.println("--------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el XML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}