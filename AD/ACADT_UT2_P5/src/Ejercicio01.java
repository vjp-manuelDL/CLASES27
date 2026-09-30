import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ejercicio01 {
    public static void main(String[] args) {
        try {
            Persona p = new Persona("Ana", "Gomez");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Escribimos en el fichero
            FileWriter escritor = new FileWriter("persona.json");
            gson.toJson(p, escritor);
            escritor.close();
            System.out.println("Fichero persona.json escrito.");

            // Leemos del fichero
            FileReader lector = new FileReader("persona.json");
            Persona pLeida = gson.fromJson(lector, Persona.class);
            lector.close();

            System.out.println("Leido -> Nombre: " + pLeida.getNombre() + ", Apellido: " + pLeida.getApellido());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}