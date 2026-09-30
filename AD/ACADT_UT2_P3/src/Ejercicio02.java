import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;

public class Ejercicio02 {
    public static void main(String[] args) {
        try {
            // Preparamos XStream
            XStream xstream = new XStream();

            // Configuramos los alias igual que al guardar
            xstream.alias("persona", Persona.class);

            // XStream por seguridad pide permiso para leer la clase
            xstream.allowTypes(new Class[] {Persona.class});

            // Leemos el XML y lo convertimos a objeto
            Persona personaLeida = (Persona) xstream.fromXML(new FileInputStream("persona.xml"));

            // Imprimimos los datos recuperados
            System.out.println("Datos leidos del XML:");
            System.out.println("Nombre: " + personaLeida.getNombre());
            System.out.println("Apellido: " + personaLeida.getApellido());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}