import com.google.gson.Gson;

public class Ejercicio01 {
    public static void main(String[] args) {
        // Creamos un libro de prueba
        Libro miLibro = new Libro("El Quijote", "Cervantes");

        // Preparamos la herramienta Gson
        Gson gson = new Gson();

        // Convertimos el libro a formato texto JSON
        String textoJson = gson.toJson(miLibro);

        // Imprimimos el resultado (saldra todo en una sola linea)
        System.out.println("JSON normal:");
        System.out.println(textoJson);
    }
}