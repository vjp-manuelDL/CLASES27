import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Ejercicio01 {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String rutaArchivo = "empleado_guardado.json";

        // Escritura en fichero JSON
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            Empleado emp = new Empleado("Ana", "Gomez", 28, Arrays.asList("Directora"));
            gson.toJson(emp, writer);
            System.out.println("Archivo JSON escrito correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Lectura desde fichero JSON
        try (FileReader reader = new FileReader(rutaArchivo)) {
            Empleado empLeido = gson.fromJson(reader, Empleado.class);
            System.out.println("Objeto leído del archivo: " + empLeido.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}