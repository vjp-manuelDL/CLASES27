import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Ejercicio02 {
    public static void main(String[] args) {
        try {
            // Preparamos la configuracion para crear nuestro lector de XML
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            // Creamos el procesador (el parser) que nos va a ayudar a leer
            SAXParser parser = parserFactory.newSAXParser();

            // Obtenemos el lector que va a recorrer el archivo
            XMLReader lectorXml = parser.getXMLReader();

            // Creamos nuestro manejador de empleados y se lo asignamos al lector
            EmpleadosHandler manejador = new EmpleadosHandler();
            lectorXml.setContentHandler(manejador);

            // Le indicamos la ruta exacta donde esta nuestro archivo
            InputSource archivoXml = new InputSource("./empleados.xml");

            // Damos la orden final de que empiece a leer el archivo
            lectorXml.parse(archivoXml);

            // Obtenemos la lista que el manejador ha rellenado con los datos
            ArrayList<Empleado> lista = manejador.getListaEmpleados();

            // Ordenamos la lista por sueldo de menor a mayor
            Collections.sort(lista);

            // Imprimimos el resultado recorriendo la lista
            for (int i = 0; i < lista.size(); i++) {
                Empleado e = lista.get(i);
                // Imprimimos los datos con el formato que pide el ejercicio
                System.out.println(e.getNombre() + " " + e.getApellido() + ", sueldo: " + e.getSueldo() + " euros");
            }

        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

// Clase manejadora que lee las etiquetas y extrae la informacion
class EmpleadosHandler extends DefaultHandler {

    // Lista donde vamos a ir guardando los empleados que leemos
    private ArrayList<Empleado> listaEmpleados = new ArrayList<>();

    // Variable para guardar temporalmente el empleado que estamos leyendo en ese momento
    private Empleado empleadoActual;

    // Variable para ir guardando el texto que hay dentro de cada etiqueta
    private String textoActual;

    public ArrayList<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Cuando encontramos una etiqueta "empleado", preparamos un objeto vacio para llenarlo
        if (qName.equals("empleado")) {
            empleadoActual = new Empleado();
        }
        // Limpiamos el texto al abrir cualquier etiqueta nueva para no mezclar datos
        textoActual = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Leemos el texto que hay entre las etiquetas
        String texto = new String(ch, start, length);

        // Usamos trim() para quitar los espacios en blanco que sobran a los lados y lo guardamos
        textoActual += texto.trim();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Cuando se cierra una etiqueta, miramos cual es y guardamos su dato en el empleado
        if (qName.equals("nombre")) {
            empleadoActual.setNombre(textoActual);
        } else if (qName.equals("apellido")) {
            empleadoActual.setApellido(textoActual);
        } else if (qName.equals("sueldo")) {
            // Convertimos el texto a numero entero para poder ordenarlo matematicamente luego
            empleadoActual.setSueldo(Integer.parseInt(textoActual));
        } else if (qName.equals("empleado")) {
            // Cuando se cierra el empleado completo, ya tiene todos sus datos y lo añadimos a la lista
            listaEmpleados.add(empleadoActual);
        }
    }
}