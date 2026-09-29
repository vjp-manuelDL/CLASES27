import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Ejercicio04 {
    public static void main(String[] args) {
        try {
            // 1. Aquí se construye el parser y se carga el documento Libros.xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("Libros.xml"));
            doc.getDocumentElement().normalize();

            // 2. Aquí se obtienen todos los nodos "libro"
            NodeList listaLibros = doc.getElementsByTagName("libro");

            System.out.println("--- LISTADO DE LIBROS ---");
            // 3. Aquí se recorre la lista de nodos
            for (int i = 0; i < listaLibros.getLength(); i++) {
                Node nodo = listaLibros.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    // AQUÍ ESTÁ LA CLAVE: Se lee el atributo ISBN directamente del elemento
                    String isbn = elemento.getAttribute("ISBN");

                    // Se leen los nodos hijos de forma normal
                    String titulo = elemento.getElementsByTagName("titulo").item(0).getTextContent();
                    String autor = elemento.getElementsByTagName("autor").item(0).getTextContent();
                    String editorial = elemento.getElementsByTagName("editorial").item(0).getTextContent();

                    System.out.println("ISBN: " + isbn);
                    System.out.println("Título: " + titulo);
                    System.out.println("Autor: " + autor + " | Editorial: " + editorial);
                    System.out.println("--------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}