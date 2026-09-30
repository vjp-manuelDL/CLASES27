import com.thoughtworks.xstream.XStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Ejercicio06 {
    public static void main(String[] args) {
        try {
            // Cargamos de nuevo el XML en memoria para pasarlo a JSON
            XStream xstream = new XStream();
            xstream.allowTypes(new Class[] { Esdla.class });

            // Añadimos las mismas reglas que usamos en el Ejercicio04:
            xstream.ignoreUnknownElements();
            xstream.alias("esdla", Esdla.class);
            xstream.alias("personaje", String.class);

            FileInputStream archivoXml = new FileInputStream("esdla.xml");
            Esdla datosEsdla = (Esdla) xstream.fromXML(archivoXml);
            archivoXml.close();

            // Guardamos a esdla.json en formato pretty
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            FileWriter escritorJson = new FileWriter("esdla.json");
            gsonPretty.toJson(datosEsdla, escritorJson);
            escritorJson.close();

            System.out.println("6. Fichero 'esdla.json' generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}