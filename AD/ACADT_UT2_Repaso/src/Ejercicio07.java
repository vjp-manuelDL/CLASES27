import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;

public class Ejercicio07 {
    public static void main(String[] args) {
        try {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

            // Leemos el fichero json creado anteriormente
            FileReader lectorJson = new FileReader("esdla.json");
            Esdla jsonRecuperado = gsonPretty.fromJson(lectorJson, Esdla.class);
            lectorJson.close();

            System.out.println("7. Contenido de esdla.json cargado y mostrado en formato amigable:");
            System.out.println(gsonPretty.toJson(jsonRecuperado));

        } catch (Exception e) {
            System.out.println("Asegurate de haber ejecutado antes el ejercicio 6 para generar el json.");
            e.printStackTrace();
        }
    }
}