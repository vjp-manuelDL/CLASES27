import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;

public class Ejercicio04 {
    public static void main(String[] args) {
        try {
            XStream xstream = new XStream();

            // Permisos e ignorar lo que no este en nuestra clase
            xstream.allowTypes(new Class[] { Esdla.class });
            xstream.ignoreUnknownElements();

            // Alias principal
            xstream.alias("esdla", Esdla.class);

            // ÚNICA regla necesaria para la lista: los elementos de dentro son Strings
            xstream.alias("personaje", String.class);

            FileInputStream archivoXml = new FileInputStream("esdla.xml");
            Esdla datosEsdla = (Esdla) xstream.fromXML(archivoXml);
            archivoXml.close();

            System.out.println("4. XML cargado con XStream correctamente.");
            System.out.println("Titulo leido: " + datosEsdla.getTitulo());
            System.out.println("Personajes leidos: " + datosEsdla.getPersonajes());

        } catch (Exception e) {
            System.out.println("Asegurate de tener el archivo esdla.xml en la raiz.");
            e.printStackTrace();
        }
    }
}