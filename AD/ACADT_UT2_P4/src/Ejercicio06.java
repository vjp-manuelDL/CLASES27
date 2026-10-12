import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Ejercicio06 {

    public static void main(String[] args) {
        List<Libro> listaLibros = new ArrayList<>();
        listaLibros.add(new Libro("El Quijote", "Miguel de Cervantes", 1605));
        listaLibros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", 1967));

        // 1. Serializar la lista a JSON "pretty" usando un método
        String jsonResultado = serializarLibros(listaLibros);

        System.out.println("--- JSON de Libros (Pretty) ---");
        System.out.println(jsonResultado);

        System.out.println("\n--- Libros Deserializados ---");
        // 2. Pasar el JSON como parámetro a otro método para deserializar
        deserializarLibros(jsonResultado);
    }

    public static String serializarLibros(List<Libro> libros) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        return prettyGson.toJson(libros);
    }

    public static void deserializarLibros(String json) {
        Gson gson = new Gson();
        Type tipoListaLibros = new TypeToken<List<Libro>>(){}.getType();
        List<Libro> librosDeserializados = gson.fromJson(json, tipoListaLibros);

        for (Libro libro : librosDeserializados) {
            System.out.println(libro.toString());
        }
    }
}