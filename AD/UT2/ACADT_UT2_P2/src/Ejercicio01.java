import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Ejercicio01 {
    public static void main(String[] args) {
        try {
            // Para poder crear nuestro lector de XML
            SAXParserFactory fabrica = SAXParserFactory.newInstance();

            // Creamos el procesador (el parser) que nos va a ayudar a leer
            SAXParser parser = fabrica.newSAXParser();

            // Obtenemos el lector  que va a recorrer el archivo
            XMLReader lectorXml = parser.getXMLReader();

            // Le decimos a nuestro lector que utilice la clase AlumnosReader
            // para saber que hacer cada vez que encuentre una etiqueta
            lectorXml.setContentHandler(new AlumnosReader());

            // Le indicamos la ruta exacta donde esta nuestro archivo
            InputSource archivoXml = new InputSource("./alumnos.xml");

            // Damos la orden final de que empiece a leer el archivo
            lectorXml.parse(archivoXml);

        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

// Esta es la clase que decide que hacer cada vez que SAX lee un trozo de archivo
class AlumnosReader extends DefaultHandler {

    @Override
    public void startDocument() throws SAXException {
        // Se ejecuta una sola vez al abrir el archivo
        System.out.println("Inicio del documento");
    }

    @Override
    public void endDocument() throws SAXException {
        // Se ejecuta una sola vez al terminar de leer el archivo
        System.out.println("Fin del documento");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Se ejecuta cada vez que encuentra una etiqueta de apertura (
        System.out.println("\t Inicio del Elemento: " + qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Se ejecuta cada vez que encuentra una etiqueta de cierre (ejemplo: </nombre>)
        System.out.println("\t Final del Elemento: " + qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Aqui recogemos el texto que hay entre las etiquetas
        String texto = new String(ch, start, length);

        // Usamos trim() para borrar espacios, tabuladores y saltos de linea sobrantes
        texto = texto.trim();

        // Con este if evitamos que imprima lineas vacias por culpa de los espacios
        if (!texto.isEmpty()) {
            System.out.println("\t Valor del Elemento: " + texto);
        }
    }
}