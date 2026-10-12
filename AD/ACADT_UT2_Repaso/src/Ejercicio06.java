import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;

public class Ejercicio06 {
    public static void main(String[] args) {
        Esdla datos = Ejercicio04.cargarXML();

        if (datos != null) {
            // a. Añadir nueva ciudad
            datos.getCiudades().add(new Ciudad("Osgiliath", "Gondor"));

            // b. Eliminar el primer personaje de la lista de hobbits (usando getPersonajes en plural)
            if (datos.getPersonajes() != null && datos.getPersonajes().getHobbits() != null && !datos.getPersonajes().getHobbits().isEmpty()) {
                datos.getPersonajes().getHobbits().remove(0);
            }

            // Guardar en esdla.json con formato pretty
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("esdla.json")) {
                gson.toJson(datos, writer);
                System.out.println("Archivo 'esdla.json' generado correctamente.");
            } catch (Exception e) {
                System.out.println("Error al guardar JSON: " + e.getMessage());
            }
        }
    }
}