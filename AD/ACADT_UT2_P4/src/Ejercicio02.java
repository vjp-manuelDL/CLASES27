import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ejercicio02 {
    public static void main(String[] args) {
        Libro miLibro = new Libro("El Señor de los Anillos", "Tolkien");

        // Configuramos Gson para que el texto salga con saltos de linea y espacios (Pretty Printing)
        Gson gsonBonito = new GsonBuilder().setPrettyPrinting().create();

        // Convertimos el libro a JSON
        String textoJson = gsonBonito.toJson(miLibro);

        System.out.println("JSON con formato Pretty:");
        System.out.println(textoJson);
    }
}