import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

public class Ejercicio03 {
    public static void main(String[] args) {
        Libro miLibro = new Libro("Harry Potter", "J.K. Rowling");

        // Creamos un mapa para poder ponerle una etiqueta principal al JSON
        Map<String, Libro> contenedor = new HashMap<>();

        // Le asignamos el nombre "MiLibroFavorito" al objeto
        contenedor.put("MiLibroFavorito", miLibro);

        Gson gsonBonito = new GsonBuilder().setPrettyPrinting().create();
        String textoJson = gsonBonito.toJson(contenedor);

        System.out.println("JSON con nombre de objeto:");
        System.out.println(textoJson);
    }
}