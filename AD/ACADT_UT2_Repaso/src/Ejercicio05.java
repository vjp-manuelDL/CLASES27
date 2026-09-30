import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Ejercicio05 {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(new File("esdla.xml"));
            doc.getDocumentElement().normalize();

            // a. Modificar elemento autor
            Element autor = (Element) doc.getElementsByTagName("autor").item(0);
            if (autor != null) {
                autor.setTextContent("");
                Element nombre = doc.createElement("nombre");
                nombre.setTextContent("John Ronald Reuel Tolkien");
                Element diminutivo = doc.createElement("diminutivo");
                diminutivo.setTextContent("J. R. R. Tolkien");
                Element nacimiento = doc.createElement("nacimiento");
                nacimiento.setTextContent("1892");
                Element fallecimiento = doc.createElement("fallecimiento");
                fallecimiento.setTextContent("1892");

                autor.appendChild(nombre);
                autor.appendChild(diminutivo);
                autor.appendChild(nacimiento);
                autor.appendChild(fallecimiento);
            }

            // b. Añadir nueva ciudad "Osgiliath" y facción "Gondor"
            Element nuevaCiudad = doc.createElement("ciudad");
            nuevaCiudad.setAttribute("nombre", "Osgiliath");
            nuevaCiudad.setAttribute("faccion", "Gondor");
            doc.getDocumentElement().appendChild(nuevaCiudad);

            // Guardamos el DOM actualizado en "esdla_actualizado.xml"
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource origen = new DOMSource(doc);
            StreamResult destino = new StreamResult(new File("esdla_actualizado.xml"));
            transformer.transform(origen, destino);

            System.out.println("5. Fichero 'esdla_actualizado.xml' creado correctamente con DOM.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}