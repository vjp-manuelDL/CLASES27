import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;

public class Ejercicio05 {
    public static void main(String[] args) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("esdla.xml");

            // a. Modificar el elemento autor
            NodeList listaAutores = doc.getElementsByTagName("autor");
            if (listaAutores.getLength() > 0) {
                Node nodoAutor = listaAutores.item(0);
                nodoAutor.setTextContent("");

                Element nombre = doc.createElement("nombre");
                nombre.setTextContent("John Ronald Reuel Tolkien");
                Element diminutivo = doc.createElement("diminutivo");
                diminutivo.setTextContent("J. R. R. Tolkien");
                Element nacimiento = doc.createElement("nacimiento");
                nacimiento.setTextContent("1892");
                Element fallecimiento = doc.createElement("fallecimiento");
                fallecimiento.setTextContent("1973");

                nodoAutor.appendChild(nombre);
                nodoAutor.appendChild(diminutivo);
                nodoAutor.appendChild(nacimiento);
                nodoAutor.appendChild(fallecimiento);
            }

            // b. Añadir nueva ciudad (Osgiliath, Gondor)
            NodeList listaRaiz = doc.getChildNodes();
            if (listaRaiz.getLength() > 0) {
                Node raiz = listaRaiz.item(0);

                Element nuevaCiudad = doc.createElement("ciudad");
                Element nombreC = doc.createElement("nombre");
                nombreC.setTextContent("Osgiliath");
                Element faccionC = doc.createElement("faccion");
                faccionC.setTextContent("Gondor");

                nuevaCiudad.appendChild(nombreC);
                nuevaCiudad.appendChild(faccionC);
                raiz.appendChild(nuevaCiudad);
            }

            // c. Eliminar el último personaje de las listas
            NodeList listaHobbits = doc.getElementsByTagName("hobbit");
            if (listaHobbits.getLength() > 0) {
                Node ultimoHobbit = listaHobbits.item(listaHobbits.getLength() - 1);
                ultimoHobbit.getParentNode().removeChild(ultimoHobbit);
            }

            // Guardar cambios a esdla_actualizado.xml
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("esdla_actualizado.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo 'esdla_actualizado.xml' creado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}