import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;

public class Ejercicio07 {
    public static void main(String[] args) {
        Gson gsonLectura = new Gson();
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

        try (FileReader reader = new FileReader("esdla.json")) {
            Esdla datosCargados = gsonLectura.fromJson(reader, Esdla.class);

            System.out.println("--- Contenido de esdla.json ---");
            System.out.println(gsonPretty.toJson(datosCargados));

        } catch (Exception e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }
}