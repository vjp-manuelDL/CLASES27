import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;

public class Ejercicio04 {
    public static Esdla cargarXML() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        xstream.alias("esdla", Esdla.class);
        xstream.alias("ciudad", Ciudad.class);
        xstream.alias("personajes", Personaje.class);

        // Como en el XML todas usan la etiqueta interna <personaje>, mapeamos cada lista a "personaje"
        xstream.addImplicitCollection(Personaje.class, "hobbits", "personaje", String.class);
        xstream.addImplicitCollection(Personaje.class, "hombres", "personaje", String.class);
        xstream.addImplicitCollection(Personaje.class, "elfos", "personaje", String.class);
        xstream.addImplicitCollection(Personaje.class, "enanos", "personaje", String.class);
        xstream.addImplicitCollection(Personaje.class, "magos", "personaje", String.class);
        xstream.addImplicitCollection(Personaje.class, "villanos", "personaje", String.class);

        try {
            Esdla datos = (Esdla) xstream.fromXML(new File("esdla.xml"));
            System.out.println("XML cargado a objetos Java correctamente.");
            return datos;
        } catch (Exception e) {
            System.out.println("Error al cargar XML: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        cargarXML();
    }
}