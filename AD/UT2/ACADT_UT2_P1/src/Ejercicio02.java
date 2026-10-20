import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Ejercicio02 {
    public static void main(String[] args) {
        try {
            // 1. Simulamos la lectura de objetos Libro desde un fichero
            ArrayList<Libro> listaLibros = new ArrayList<>();
            listaLibros.add(new Libro("239-87-9964-088-4", "Acceso a Datos", "Alicia Ramos", "Garceta"));
            listaLibros.add(new Libro("978-84-205-3987-1", "Java a fondo", "Carlos Pérez", "Anaya"));

            // 2. Aquí se crea el parser DOM y el documento vacío
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // 3. Aquí se crea el nodo raíz
            Element raiz = doc.createElement("libros");
            doc.appendChild(raiz);

            // 4. Aquí se recorren los objetos Libro y se insertan
            for (Libro l : listaLibros) {
                Element nodoLibro = doc.createElement("libro");

                // AQUÍ ESTÁ LA CLAVE: El ISBN va como atributo, no como nodo hijo
                nodoLibro.setAttribute("ISBN", l.getIsbn());

                // El resto de campos van como nodos hijos
                nodoLibro.appendChild(crearNodoTexto(doc, "titulo", l.getTitulo()));
                nodoLibro.appendChild(crearNodoTexto(doc, "autor", l.getAutor()));
                nodoLibro.appendChild(crearNodoTexto(doc, "editorial", l.getEditorial()));

                raiz.appendChild(nodoLibro);
            }

            // 5. Aquí se vuelca el documento al fichero Libros.xml
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new File("Libros.xml")));

            System.out.println("Fichero Libros.xml creado con éxito.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Element crearNodoTexto(Document doc, String etiqueta, String valor) {
        Element nodo = doc.createElement(etiqueta);
        nodo.appendChild(doc.createTextNode(valor));
        return nodo;
    }
}