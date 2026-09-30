import com.thoughtworks.xstream.XStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Ejercicio03 {
    public static void main(String[] args) {
        try {
            // 1. Leemos el XML con XStream
            XStream xstream = new XStream();
            xstream.allowTypes(new Class[] { ListaLibros.class, LibroXml.class });
            xstream.alias("libros", ListaLibros.class);
            xstream.alias("libro", LibroXml.class);

            // Esta linea le dice a XStream que la etiqueta "libro" dentro de ListaLibros es una coleccion
            xstream.addImplicitCollection(ListaLibros.class, "libro");

            FileInputStream archivoXml = new FileInputStream("libros-14.xml");
            ListaLibros listaLeida = (ListaLibros) xstream.fromXML(archivoXml);
            archivoXml.close();
            System.out.println("XML leido con XStream.");

            // 2. Escribimos el JSON con Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter escritorJson = new FileWriter("libros.json");

            gson.toJson(listaLeida, escritorJson);
            escritorJson.close();

            System.out.println("Archivo libros.json creado con exito.");

        } catch (Exception e) {
            System.out.println("Asegurate de tener el archivo libros-14.xml en la raiz.");
            e.printStackTrace();
        }
    }
}