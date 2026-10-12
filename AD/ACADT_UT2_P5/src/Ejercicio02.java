import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio02 {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("productos.json")) {
            // Lee el archivo JSON dinámicamente sin necesidad de crear una clase Producto
            JsonElement elementoJson = JsonParser.parseReader(reader);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            System.out.println("--- Contenido de productos.json ---");
            System.out.println(gson.toJson(elementoJson));

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}