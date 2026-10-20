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

public class Ejercicio01 {
    public static void main(String[] args) {
        try {
            // 1. Simulamos la lectura de objetos Contacto desde un fichero
            ArrayList<Contacto> listaContactos = new ArrayList<>();
            listaContactos.add(new Contacto("Ana", "García López", "ana@email.com", "600111222"));
            listaContactos.add(new Contacto("Luis", "Martín Ruiz", "luis@email.com", "600333444"));

            // 2. Creamos la fábrica y el constructor de documentos (Parser DOM)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // 3. Creamos el nodo raíz del documento XML
            Element raiz = doc.createElement("contactos");
            doc.appendChild(raiz);

            // 4. Recorremos los objetos y se insertan como nodos en el documento
            for (Contacto c : listaContactos) {
                Element nodoContacto = doc.createElement("contacto");

                // Crear y añadir hijos (nombre, apellidos, email, telefono)
                nodoContacto.appendChild(crearNodoTexto(doc, "nombre", c.getNombre()));
                nodoContacto.appendChild(crearNodoTexto(doc, "apellidos", c.getApellidos()));
                nodoContacto.appendChild(crearNodoTexto(doc, "email", c.getEmail()));
                nodoContacto.appendChild(crearNodoTexto(doc, "telefono", c.getTelefono()));

                raiz.appendChild(nodoContacto);
            }

            // 5. Usamos Transformer para volcar el arbol DOM a un fichero en disco
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Para que quede formateado

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Contactos.xml"));
            transformer.transform(source, result);

            System.out.println("Fichero Contactos.xml creado con éxito.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo auxiliar para crear nodos de texto de forma limpia
    private static Element crearNodoTexto(Document doc, String etiqueta, String valor) {
        Element nodo = doc.createElement(etiqueta);
        nodo.appendChild(doc.createTextNode(valor));
        return nodo;
    }
}