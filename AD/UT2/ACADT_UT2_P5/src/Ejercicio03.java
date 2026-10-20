import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Ejercicio03 {
    public static void main(String[] args) {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        // Indicamos que la etiqueta raíz <libros> equivale a una Lista en Java
        xstream.alias("libros", List.class);
        // Indicamos que cada etiqueta <libro> equivale a nuestra clase Libro
        xstream.alias("libro", Libro.class);

        try {
            Object datosXml = xstream.fromXML(new File("libros-14.xml"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("libros.json")) {
                gson.toJson(datosXml, writer);
                System.out.println("Archivo 'libros.json' generado exitosamente a partir del XML.");
            }

        } catch (Exception e) {
            System.out.println("Se produjo un error en la conversión: " + e.getMessage());
        }
    }
}