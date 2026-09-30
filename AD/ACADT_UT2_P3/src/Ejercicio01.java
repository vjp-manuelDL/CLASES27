import com.thoughtworks.xstream.XStream;
import java.io.FileOutputStream;

public class Ejercicio01 {
    public static void main(String[] args) {
        try {
            // Creamos un objeto de prueba pasándole solo nombre y apellido
            Persona persona = new Persona("Juan", "Perez");

            // Preparamos XStream
            XStream xstream = new XStream();

            // Quitamos el nombre largo del paquete y lo llamamos solo "persona"
            xstream.alias("persona", Persona.class);

            // Convertimos a XML y guardamos en el archivo
            xstream.toXML(persona, new FileOutputStream("persona.xml"));
            System.out.println("Archivo persona.xml creado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}