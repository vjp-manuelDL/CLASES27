import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio06 {
    public static void main(String[] args) {
        // Preparamos nuestra lista de libros
        List<Libro> miLista = new ArrayList<>();
        miLista.add(new Libro("Cien anos de soledad", "Gabriel Garcia Marquez"));
        miLista.add(new Libro("1984", "George Orwell"));

        // Llamamos al primer metodo para convertir la lista a JSON bonito
        String jsonGenerado = serializarAJson(miLista);

        System.out.println("--- RESULTADO DE SERIALIZAR ---");
        System.out.println(jsonGenerado);

        // Llamamos al segundo metodo pasandole el texto que acabamos de generar
        deserializarDesdeJson(jsonGenerado);
    }

    // Metodo 1: Convierte una lista de libros a texto JSON
    public static String serializarAJson(List<Libro> lista) {
        Gson gsonBonito = new GsonBuilder().setPrettyPrinting().create();
        return gsonBonito.toJson(lista);
    }

    // Metodo 2: Recibe un texto JSON y lo convierte de vuelta a libros
    public static void deserializarDesdeJson(String textoJson) {
        Gson gson = new Gson();

        // Lo leemos como un Array de libros
        Libro[] librosRecuperados = gson.fromJson(textoJson, Libro[].class);

        System.out.println("\n--- RESULTADO DE DESERIALIZAR ---");
        for (int i = 0; i < librosRecuperados.length; i++) {
            System.out.println("Libro recuperado: " + librosRecuperados[i].getTitulo());
        }
    }
}